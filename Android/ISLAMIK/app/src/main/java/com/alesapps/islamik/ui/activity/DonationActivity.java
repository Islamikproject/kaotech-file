package com.alesapps.islamik.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.ExceptionListener;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.PaymentModel;
import com.alesapps.islamik.utils.MessageUtil;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.model.Charge;
import java.util.HashMap;
import java.util.Map;

public class DonationActivity extends BaseActionBarActivity implements View.OnClickListener {
	public static DonationActivity instance = null;

    EditText edt_name;
	EditText edt_card_number;
	EditText edt_month;
    EditText edt_year;
	EditText edt_cvc;
    EditText edt_amount;
    Button btn_pay;

    String chargeId = "";
    public static ParseObject mSermonObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;
		SetTitle(R.string.donation, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_donation);

		edt_name = findViewById(R.id.edt_name);
		edt_card_number = findViewById(R.id.edt_card_number);
		edt_month = findViewById(R.id.edt_month);
		edt_year = findViewById(R.id.edt_year);
		edt_cvc = findViewById(R.id.edt_cvc);
        edt_amount = findViewById(R.id.edt_amount);
        btn_pay = findViewById(R.id.btn_pay);
        findViewById(R.id.btn_pay).setOnClickListener(this);
        initialize();
	}

	private void initialize() {
	    edt_amount.setText(String.valueOf(mSermonObj.getDouble(ParseConstants.KEY_AMOUNT)));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_pay: {
                if (isValid())
                    saveCreditCard();
            } break;
        }
    }

    private boolean isValid() {
	    String name = edt_name.getText().toString().trim();
	    if (TextUtils.isEmpty(name)) {
	        MessageUtil.showError(instance, R.string.valid_No_name);
	        edt_name.requestFocus();
	        return false;
        }
		String cardNumber = edt_card_number.getText().toString().trim();
		if (TextUtils.isEmpty(cardNumber)) {
			MessageUtil.showError(instance, R.string.valid_No_card_number);
            edt_card_number.requestFocus();
			return false;
		}
		String CVC = edt_cvc.getText().toString().trim();
		if (TextUtils.isEmpty(CVC)) {
			MessageUtil.showError(instance, R.string.valid_No_cvc);
            edt_cvc.requestFocus();
			return false;
		}
        String expiredYear = edt_year.getText().toString().trim();
        if (TextUtils.isEmpty(expiredYear)) {
            MessageUtil.showError(instance, R.string.valid_No_year);
            edt_year.requestFocus();
            return false;
        }
        if (Integer.parseInt(expiredYear) < 1970 || Integer.parseInt(expiredYear) > 2200) {
            MessageUtil.showError(instance, R.string.valid_Invalid_year);
            edt_year.requestFocus();
            return false;
        }
        String expiredMonth = edt_month.getText().toString().trim();
        if (TextUtils.isEmpty(expiredMonth)) {
            MessageUtil.showError(instance, R.string.valid_No_month);
            edt_month.requestFocus();
            return false;
        }
        if (Integer.parseInt(expiredMonth) == 0 || Integer.parseInt(expiredMonth) > 12) {
            MessageUtil.showError(instance, R.string.valid_Invalid_month);
            edt_month.requestFocus();
            return false;
        }
        String amount = edt_amount.getText().toString().trim();
        if (TextUtils.isEmpty(amount) || Double.valueOf(amount) == 0) {
            MessageUtil.showError(instance, R.string.valid_No_amount);
            edt_amount.requestFocus();
            return false;
        }
        ParseUser userObj = mSermonObj.getParseUser(ParseConstants.KEY_OWNER);
        if (TextUtils.isEmpty(userObj.getString(ParseConstants.KEY_ACCOUNT_ID))) {
            MessageUtil.showError(instance, R.string.msg_stripe_connect);
            return false;
        }
		return true;
	}

    public void saveCreditCard() {
        String cardnumber = edt_card_number.getText().toString().trim();
        int year = Integer.parseInt(edt_year.getText().toString().trim());
        int month = Integer.parseInt(edt_month.getText().toString().trim());
        String cvc = edt_cvc.getText().toString().trim();
        Card card = new Card(cardnumber, month, year, cvc);
        card.setCurrency(AppConstant.STRIPE_CURRENCY);
        boolean validation = card.validateCard();
        if (validation) {
            dlg_progress.show();
            new Stripe().createToken(
                    card,
                    AppConstant.STRIPE_PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            chargeCustomer(token);
                        }
                        public void onError(Exception error) {
                            MessageUtil.showToast(instance, error.getLocalizedMessage());
                            dlg_progress.hide();
                        }
                    });
        } else if (!card.validateNumber()) {
            MessageUtil.showError(instance, "The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            MessageUtil.showError(instance, "The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            MessageUtil.showError(instance, "The CVC code that you entered is invalid");
        } else {
            MessageUtil.showError(instance, "The card details that you entered are invalid");
        }
    }

    public void chargeCustomer(Token token) {
        ParseUser userObj = mSermonObj.getParseUser(ParseConstants.KEY_OWNER);
        final Map<String, Object> chargeParams = new HashMap<String, Object>();
        double amount = Double.valueOf(edt_amount.getText().toString()) * 100;
        chargeParams.put("amount", (int) amount);
        chargeParams.put("currency", AppConstant.STRIPE_CURRENCY);
        chargeParams.put("source", token.getId());
        chargeParams.put("capture", true);
        chargeParams.put("destination", userObj.getString(ParseConstants.KEY_ACCOUNT_ID));
        String description = edt_name.getText().toString().trim() + " paid to " + userObj.getString(ParseConstants.KEY_MOSQUE);
        chargeParams.put("description", description);

        new AsyncTask<String, String, String>() {
            Charge charge;

            @Override
            protected String doInBackground(String... params) {
                try {
                    com.stripe.Stripe.apiKey = AppConstant.STRIPE_API_KEY;
                    charge = Charge.create(chargeParams);
                } catch (Exception e) {
                    return e.toString();
                }
                return "success";
            }

            protected void onPostExecute(String result) {
                if (result.equals("success")) {
                    MessageUtil.showToast(instance, "Card Charged : " + charge.getCreated() + "\nPaid : " + charge.getPaid());
                    chargeId = charge.getId();
                    saveDonation();
                } else {
                    MessageUtil.showError(instance, result);
                    dlg_progress.hide();
                }
            };
        }.execute();
    }

    private void saveDonation() {
        PaymentModel model = new PaymentModel();
        model.name = edt_name.getText().toString().trim();
        model.toUser = mSermonObj.getParseUser(ParseConstants.KEY_OWNER);
        model.sermon = mSermonObj;
        model.amount = Double.valueOf(edt_amount.getText().toString());
        model.chargeId = chargeId;
        model.type = mSermonObj.getInt(ParseConstants.KEY_TYPE);
        dlg_progress.show();
        PaymentModel.Register(model, new ExceptionListener() {
            @Override
            public void done(String error) {
                dlg_progress.hide();
                if (error == null) {
                    MessageUtil.showToast(instance, R.string.success_donation);
                    myBack();
                } else {
                    MessageUtil.showToast(instance, error);
                }
            }
        });
    }
}
