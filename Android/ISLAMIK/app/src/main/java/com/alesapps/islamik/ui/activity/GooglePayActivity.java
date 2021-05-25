package com.alesapps.islamik.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alesapps.islamik.Constants;
import com.alesapps.islamik.R;
import com.alesapps.islamik.utils.PaymentsUtil;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Optional;

/**
 * Transparent activity to be triggered from notification
 */
public class GooglePayActivity extends AppCompatActivity {

  // Arbitrarily-picked constant integer you define to track a request for payment data activity.
  private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Dismiss the notification UI if the activity was opened from a notification
    showPaymentsSheet();
  }

  /**
   * Handle a resolved activity from the Google Pay payment sheet.
   *
   * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
   * @param resultCode  Result code returned by the Google Pay API.
   * @param data        Intent from the Google Pay API containing payment or error data.
   * @see <a href="https://developer.android.com/training/basics/intents/result">Getting a result
   * from an Activity</a>
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    
    switch (requestCode) {

      case LOAD_PAYMENT_DATA_REQUEST_CODE:
        switch (resultCode) {

          case Activity.RESULT_OK:
            PaymentData paymentData = PaymentData.getFromIntent(data);
            handlePaymentSuccess(paymentData);
            break;

          case Activity.RESULT_CANCELED:
            // The user simply cancelled without selecting a payment method.
            break;

          case AutoResolveHelper.RESULT_ERROR:
            // Get more details on the error with – AutoResolveHelper.getStatusFromIntent(data);
            break;
        }

        // Close the activity
        finishAndRemoveTask();
    }
  }

  private void showPaymentsSheet() {

    // Fetch the price based on the user selection
    long priceCents = getIntent().getLongExtra(Constants.OPTION_PRICE_EXTRA, 100L);

    // TransactionInfo transaction = PaymentsUtil.createTransaction(price);
    Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents);
    if (!paymentDataRequestJson.isPresent()) {
      return;
    }

    PaymentDataRequest request =
        PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

    if (request != null) {
      final PaymentsClient paymentsClient = PaymentsUtil.createPaymentsClient(this);
      AutoResolveHelper.resolveTask(
          paymentsClient.loadPaymentData(request),
          this, LOAD_PAYMENT_DATA_REQUEST_CODE);
    }
  }

  /**
   * PaymentData response object contains the payment information, as well as any additional
   * requested information, such as billing and shipping address.
   *
   * @param paymentData A response object returned by Google after a payer approves payment.
   * @see <a href="https://developers.google.com/pay/api/android/reference/
   * object#PaymentData">PaymentData</a>
   */
  private void handlePaymentSuccess(PaymentData paymentData) {

    // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
    final String paymentInfo = paymentData.toJson();
    if (paymentInfo == null) {
      return;
    }

    // Remove the payment notification
//    Notifications.remove(this);

    try {
      JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");

      final JSONObject info = paymentMethodData.getJSONObject("info");
      final String billingName = info.getJSONObject("billingAddress").getString("name");
      Toast.makeText(
          this, getString(R.string.success),
          Toast.LENGTH_LONG).show();

    } catch (JSONException e) {
      throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
    }
  }
}