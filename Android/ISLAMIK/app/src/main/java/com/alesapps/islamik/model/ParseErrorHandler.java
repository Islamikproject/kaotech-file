package com.alesapps.islamik.model;

import com.alesapps.islamik.IslamikApp;
import com.alesapps.islamik.R;
import com.parse.ParseException;

public class ParseErrorHandler {
	public static String handle(ParseException pe) {
		if (pe == null) {
			return null;
		} else {
			switch (pe.getCode()) {
			case ParseException.OTHER_CAUSE:
				return IslamikApp.getContext().getString(R.string.error);
			case ParseException.CONNECTION_FAILED:
				break;
			case ParseException.OBJECT_NOT_FOUND:
				break;
			case ParseException.INVALID_QUERY:
				break;
			case ParseException.INVALID_CLASS_NAME:
				break;
			case ParseException.MISSING_OBJECT_ID:
				break;
			case ParseException.INVALID_KEY_NAME:
				break;
			case ParseException.INVALID_POINTER:
				break;
			case ParseException.INVALID_JSON:
				break;
			case ParseException.COMMAND_UNAVAILABLE:
				break;
			case ParseException.NOT_INITIALIZED:
				break;
			case ParseException.INCORRECT_TYPE:
				break;
			case ParseException.INVALID_CHANNEL_NAME:
				break;
			case ParseException.PUSH_MISCONFIGURED:
				break;
			case ParseException.OBJECT_TOO_LARGE:
				break;
			case ParseException.OPERATION_FORBIDDEN:
				break;
			case ParseException.CACHE_MISS:
				break;
			case ParseException.INVALID_NESTED_KEY:
				break;
			case ParseException.INVALID_FILE_NAME:
				break;
			case ParseException.INVALID_ACL:
				break;
			case ParseException.TIMEOUT:
				break;
			case ParseException.INVALID_EMAIL_ADDRESS:
				break;
			case ParseException.DUPLICATE_VALUE:
				break;
			case ParseException.INVALID_ROLE_NAME:
				break;
			case ParseException.EXCEEDED_QUOTA:
				break;
			case ParseException.SCRIPT_ERROR:
				break;
			case ParseException.VALIDATION_ERROR:
				break;
			case ParseException.FILE_DELETE_ERROR:
				break;
			case ParseException.REQUEST_LIMIT_EXCEEDED:
				break;
			case ParseException.INVALID_EVENT_NAME:
				break;
			case ParseException.USERNAME_MISSING:
				break;
			case ParseException.PASSWORD_MISSING:
				break;
			case ParseException.USERNAME_TAKEN:
				return "Email was already taken";
			case ParseException.EMAIL_TAKEN:
				break;
			case ParseException.EMAIL_MISSING:
				break;
			case ParseException.EMAIL_NOT_FOUND:
				break;
			case ParseException.SESSION_MISSING:
				break;
			case ParseException.MUST_CREATE_USER_THROUGH_SIGNUP:
				break;
			case ParseException.ACCOUNT_ALREADY_LINKED:
				break;
			case ParseException.INVALID_SESSION_TOKEN:
				break;
			case ParseException.LINKED_ID_MISSING:
				break;
			case ParseException.INVALID_LINKED_SESSION:
				break;
			case ParseException.UNSUPPORTED_SERVICE:
				break;
			}
		}
		return pe.getMessage();
	}
}
