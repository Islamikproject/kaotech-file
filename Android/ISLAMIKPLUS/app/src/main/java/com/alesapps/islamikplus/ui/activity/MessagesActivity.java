package com.alesapps.islamikplus.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.alesapps.islamikplus.R;
import com.alesapps.islamikplus.listener.ObjectListListener;
import com.alesapps.islamikplus.model.MessageModel;
import com.alesapps.islamikplus.model.ParseConstants;
import com.alesapps.islamikplus.model.UserModel;
import com.alesapps.islamikplus.ui.view.DragListView;
import com.alesapps.islamikplus.utils.DateTimeUtils;
import com.parse.ParseObject;
import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends BaseActionBarActivity implements View.OnClickListener, DragListView.OnRefreshLoadingMoreListener {
	public static MessagesActivity instance;
	public DragListView list_message;
	ListAdapter adapter;
	ArrayList<ParseObject> mDataList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.messages, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_messages);
		list_message = findViewById(R.id.listView);
		adapter = new ListAdapter();
		list_message.setAdapter(adapter);
		list_message.setOnRefreshListener(this);
		list_message.refresh();
	}

	@Override
	public void onDragRefresh() {
		getServerData();
	}

	@Override
	public void onDragLoadMore() {}

	private void getServerData() {
		MessageModel.GetMessageList(new ObjectListListener() {
			@Override
			public void done(List<ParseObject> objects, String error) {
				mDataList.clear();
				if (error == null && objects.size() > 0)
					mDataList.addAll(objects);
				showData();
			}
		});
	}

	private void showData() {
		adapter.notifyDataSetChanged();
		list_message.onRefreshComplete();
	}


	class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			View layout_container;
			TextView txt_name;
			TextView txt_date;
			TextView txt_topic;
			TextView txt_question;
			TextView txt_answer;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ListAdapter.ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(instance, R.layout.item_message, null);

				holder = new ListAdapter.ViewHolder();
				holder.layout_container = convertView.findViewById(R.id.layout_container);
				holder.txt_name = convertView.findViewById(R.id.txt_name);
				holder.txt_date = convertView.findViewById(R.id.txt_date);
				holder.txt_topic = convertView.findViewById(R.id.txt_topic);
				holder.txt_question = convertView.findViewById(R.id.txt_question);
				holder.txt_answer = convertView.findViewById(R.id.txt_answer);
				convertView.setTag(holder);

			} else {
				holder = (ListAdapter.ViewHolder) convertView.getTag();
			}
			MessageModel model = new MessageModel();
			model.parse(mDataList.get(position));
			holder.txt_name.setText(UserModel.GetFullName(model.owner));
			holder.txt_date.setText(DateTimeUtils.dateToString(mDataList.get(position).getCreatedAt(), DateTimeUtils.DATE_STRING_FORMAT));
			holder.txt_topic.setText(model.sermon.getString(ParseConstants.KEY_TOPIC));
			holder.txt_question.setText("Question: " + model.question);
			holder.txt_answer.setText("Answer: " + model.answer);
			return convertView;
		}
	}
}