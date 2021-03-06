package com.ijoomer.common.classes;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.TimePicker;

import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.custom.interfaces.ReportListner;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.custom.interfaces.ShareListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerDataPickerView;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;
import com.smart.android.framework.SmartAndroidActivity;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.AlertNeutral;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

public class IjoomerUtilities implements IjoomerSharedPreferences {

	private static ProgressDialog progress = null;
	private static String progressMsg = "";
	public static SmartAndroidActivity mSmartIphoneActivity;

	public static SeekBar skProgress;
	private static Geocoder geocoder;
	private static String currentSharing;

	/**
	 * This method will show the progress dialog with given message in the given
	 * activity's context.<br>
	 * The progress dialog will be non cancellable by default. User can not
	 * dismiss it by pressing back IjoomerButton.
	 * 
	 * @param msg
	 *            = String msg to be displayed in progress dialog.
	 * @param context
	 *            = Context context will be current activity's context.
	 *            <b>Note</b> : A new progress dialog will be generated on
	 *            screen each time this method is called.
	 */
	public static void showProgressDialog(String msg) {
		progressMsg = msg;

		mSmartIphoneActivity.runOnUiThread(new Runnable() {
			public void run() {
				progress = ProgressDialog.show(mSmartIphoneActivity, "", progressMsg);
			}
		});
	}

	/**
	 * This method will show the progress dialog with given message in the given
	 * activity's context.<br>
	 * The progress dialog can be set cancellable by passing appropriate flag in
	 * parameter. User can dismiss the current progress dialog by pressing back
	 * IjoomerButton if the flag is set to <b>true</b>; This method can also be
	 * called from non UI threads.
	 * 
	 * @param msg
	 *            = String msg to be displayed in progress dialog.
	 * @param context
	 *            = Context context will be current activity's context.
	 *            <b>Note</b> : A new progress dialog will be generated on
	 *            screen each time this method is called.
	 */
	public static void showProgressDialog(String msg, final boolean isCancellable) {
		progressMsg = msg;

		mSmartIphoneActivity.runOnUiThread(new Runnable() {
			public void run() {
				progress = ProgressDialog.show(mSmartIphoneActivity, "", progressMsg);
				progress.setCancelable(isCancellable);
			}
		});
	}

	/**
	 * This method will hide existing progress dialog.<br>
	 * It will not throw any Exception if there is no progress dialog on the
	 * screen and can also be called from non UI threads.
	 */
	public static void hideProgressDialog() {
		mSmartIphoneActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					if (progress.isShowing())
						progress.dismiss();
				} catch (Throwable e) {

				}
			}
		});
	}

	/**
	 * This method will generate and show the Ok dialog with given message and
	 * single message IjoomerButton.<br>
	 * 
	 * @param title
	 *            = String title will be the title of OK dialog.
	 * @param msg
	 *            = String msg will be the message in OK dialog.
	 * @param IjoomerButtonCaption
	 *            = String IjoomerButtonCaption will be the name of OK
	 *            IjoomerButton.
	 * @param target
	 *            = String target is AlertNewtral callback for OK IjoomerButton
	 *            click action.
	 */
	public static void getOKDialog(final String title, final String msg, final String IjoomerButtonCaption, final boolean isCancelable, final AlertNeutral target) {
		if (!msg.equalsIgnoreCase(mSmartIphoneActivity.getString(R.string.code704))) {

			try {
				mSmartIphoneActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(mSmartIphoneActivity);

						int imageResource = android.R.drawable.stat_sys_warning;
						Drawable image = mSmartIphoneActivity.getResources().getDrawable(imageResource);

						builder.setTitle(title).setMessage(msg).setIcon(image).setCancelable(false).setNeutralButton(IjoomerButtonCaption, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								target.NeutralMathod(dialog, id);
							}
						});

						AlertDialog alert = builder.create();
						alert.setCancelable(isCancelable);
						alert.show();
					}
				});
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public static void getCustomOkDialog(final String title, final String msg, final String IjoomerButtonCaption, final int layoutID, final CustomAlertNeutral target) {
		if (!msg.equals(mSmartIphoneActivity.getResources().getString(R.string.code704))) {
			mSmartIphoneActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					final Dialog dialog = new Dialog(mSmartIphoneActivity, android.R.style.Theme_Translucent_NoTitleBar);
					dialog.setContentView(layoutID);

					IjoomerTextView txtTitle = (IjoomerTextView) dialog.findViewById(R.id.txtTitle);
					IjoomerTextView txtMessage = (IjoomerTextView) dialog.findViewById(R.id.txtMessage);
					txtMessage.setMovementMethod(LinkMovementMethod.getInstance());
					txtMessage.setText(Html.fromHtml(msg));
					txtTitle.setText(title);
					IjoomerButton ok = (IjoomerButton) dialog.findViewById(R.id.btnOk);
					ok.setText(IjoomerButtonCaption);
					ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							target.NeutralMathod();
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
		}
	}

	public static SeekBar getLoadingDialog(final String message) {

		mSmartIphoneActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					final Dialog dialog = new Dialog(mSmartIphoneActivity, android.R.style.Theme_Translucent_NoTitleBar);
					dialog.setContentView(R.layout.ijoomer_loading_dialog);
					final IjoomerTextView txtMessage = (IjoomerTextView) dialog.findViewById(R.id.txtMessage);
					final IjoomerTextView txtProgrss = (IjoomerTextView) dialog.findViewById(R.id.txtProgrss);
					skProgress = (SeekBar) dialog.findViewById(R.id.skProgress);

					txtMessage.setText(message);
					txtProgrss.setText("0 %");
					skProgress.setMax(100);
					skProgress.setProgress(0);
					skProgress.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View arg0, MotionEvent arg1) {
							return true;
						}
					});
					skProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
						}

						@Override
						public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
							txtProgrss.setText(progress + " %");

							if (progress >= 100) {
								final Timer t = new Timer();
								t.schedule(new TimerTask() {

									@Override
									public void run() {
										t.cancel();
										if (dialog != null && dialog.isShowing())
											dialog.dismiss();
									}
								}, 10);
							}
						}
					});
					dialog.show();
				} catch (Throwable e) {
					e.printStackTrace();
				}

			}
		});
		synchronized (skProgress) {
			return skProgress;
		}
	}

	public static SeekBar getLoadingDialog(final String message, final String size) {

		mSmartIphoneActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					final Dialog dialog = new Dialog(mSmartIphoneActivity, android.R.style.Theme_Translucent_NoTitleBar);
					dialog.setContentView(R.layout.ijoomer_loading_dialog);
					final IjoomerTextView txtMessage = (IjoomerTextView) dialog.findViewById(R.id.txtMessage);
					final IjoomerTextView txtProgrss = (IjoomerTextView) dialog.findViewById(R.id.txtProgrss);
					final IjoomerTextView txtSize = (IjoomerTextView) dialog.findViewById(R.id.txtSize);
					txtSize.setVisibility(View.VISIBLE);
					txtSize.setText(mSmartIphoneActivity.getString(R.string.size) + " : " + size);
					skProgress = (SeekBar) dialog.findViewById(R.id.skProgress);

					txtMessage.setText(message);
					txtProgrss.setText("0 %");
					skProgress.setMax(100);
					skProgress.setProgress(0);
					skProgress.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View arg0, MotionEvent arg1) {
							return true;
						}
					});
					skProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
						}

						@Override
						public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
							txtProgrss.setText(progress + " %");

							if (progress >= 100) {
								final Timer t = new Timer();
								t.schedule(new TimerTask() {

									@Override
									public void run() {
										t.cancel();
										if (dialog != null && dialog.isShowing())
											dialog.dismiss();
									}
								}, 10);
							}
						}
					});
					dialog.show();
				} catch (Throwable e) {
					e.printStackTrace();
				}

			}
		});
		synchronized (skProgress) {
			return skProgress;
		}
	}

	public static void getCustomConfirmDialog(final String title, final String msg, final String okIjoomerButtonCaption, final String cancelIjoomerButtonCaption,
			final CustomAlertMagnatic target) {
		mSmartIphoneActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				final Dialog dialog = new Dialog(mSmartIphoneActivity, android.R.style.Theme_Translucent_NoTitleBar);
				dialog.setContentView(R.layout.ijoomer_confirm_dialog);

				IjoomerTextView txtTitle = (IjoomerTextView) dialog.findViewById(R.id.txtTitle);
				IjoomerTextView txtMessage = (IjoomerTextView) dialog.findViewById(R.id.txtMessage);
				txtMessage.setText(msg);
				txtTitle.setText(title);
				IjoomerButton ok = (IjoomerButton) dialog.findViewById(R.id.btnOk);
				IjoomerButton btnCancel = (IjoomerButton) dialog.findViewById(R.id.btnCancel);
				ok.setText(okIjoomerButtonCaption);
				btnCancel.setText(cancelIjoomerButtonCaption);

				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						target.PositiveMathod();
						dialog.dismiss();
					}
				});
				btnCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						target.NegativeMathod();
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}

	public static void getShareDialog(final ShareListner target) {

		final ArrayList<HashMap<String, Object>> selectedData = new ArrayList<HashMap<String, Object>>();
		int popupWidth = mSmartIphoneActivity.getWindowManager().getDefaultDisplay().getWidth();
		int popupHeight = mSmartIphoneActivity.getWindowManager().getDefaultDisplay().getHeight() - ((SmartActivity) mSmartIphoneActivity).convertSizeToDeviceDependent(50);

		LayoutInflater layoutInflater = (LayoutInflater) mSmartIphoneActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = layoutInflater.inflate(R.layout.ijoomer_share_dialog, null);

		final PopupWindow popup = new PopupWindow(mSmartIphoneActivity);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

		final LinearLayout lnrSayAboutStory = (LinearLayout) layout.findViewById(R.id.lnrSayAboutStory);
		ImageView imgShareFacebook = (ImageView) layout.findViewById(R.id.imgShareFacebook);
		ImageView imgShareTwitter = (ImageView) layout.findViewById(R.id.imgShareTwitter);
		ImageView imgShareAddEmail = (ImageView) layout.findViewById(R.id.imgShareAddEmail);
		ImageView imgShareClose = (ImageView) layout.findViewById(R.id.imgShareClose);
		final IjoomerEditText edtShareEmail = (IjoomerEditText) layout.findViewById(R.id.edtShareEmail);
		final IjoomerEditText edtStory = (IjoomerEditText) layout.findViewById(R.id.edtStory);
		final IjoomerEditText edtShareEmailMessage = (IjoomerEditText) layout.findViewById(R.id.edtShareEmailMessage);
		IjoomerButton btnSend = (IjoomerButton) layout.findViewById(R.id.btnSend);
		IjoomerButton btnCancel = (IjoomerButton) layout.findViewById(R.id.btnCancel);
		IjoomerButton btnCancelStory = (IjoomerButton) layout.findViewById(R.id.btnCancelStory);
		IjoomerButton btnShareStory = (IjoomerButton) layout.findViewById(R.id.btnShareStory);

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popup.dismiss();
			}
		});

		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (edtShareEmail.getText().toString().trim().length() > 0) {
					target.onClick("email", edtShareEmail.getText().toString(), edtShareEmailMessage.getText().toString().trim());
					popup.dismiss();
				} else {
					((SmartActivity) mSmartIphoneActivity).ting((mSmartIphoneActivity.getString(R.string.validation_value_required)));
				}
			}
		});
		imgShareFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				edtStory.requestFocus();
				currentSharing = "facebook";
				lnrSayAboutStory.setVisibility(View.VISIBLE);
			}
		});

		btnShareStory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				target.onClick(currentSharing, null, edtStory.getText().toString().trim());
				popup.dismiss();
			}
		});

		btnCancelStory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lnrSayAboutStory.setVisibility(View.GONE);
			}
		});

		imgShareTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edtStory.requestFocus();
				currentSharing = "twitter";
				lnrSayAboutStory.setVisibility(View.VISIBLE);
			}
		});
		imgShareAddEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getContactDialog(selectedData, new ShareListner() {

					@SuppressWarnings("unchecked")
					@Override
					public void onClick(String shareOn, Object value, String message) {
						selectedData.clear();
						selectedData.addAll((ArrayList<HashMap<String, Object>>) value);
						edtShareEmail.setText(message);
					}
				});
			}
		});
		imgShareClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popup.dismiss();
			}
		});

	}

	public static void getContactDialog(final ArrayList<HashMap<String, Object>> selected, final ShareListner target) {

		final SmartListAdapterWithHolder adapter;

		int popupWidth = mSmartIphoneActivity.getWindowManager().getDefaultDisplay().getWidth() - ((SmartActivity) mSmartIphoneActivity).convertSizeToDeviceDependent(50);
		int popupHeight = mSmartIphoneActivity.getWindowManager().getDefaultDisplay().getHeight() - ((SmartActivity) mSmartIphoneActivity).convertSizeToDeviceDependent(130);

		LayoutInflater layoutInflater = (LayoutInflater) mSmartIphoneActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.ijoomer_contact_mail_dialog, null);

		final PopupWindow popup = new PopupWindow(mSmartIphoneActivity);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

		final ListView lstContact = (ListView) layout.findViewById(R.id.lstContact);
		IjoomerButton btnCancel = (IjoomerButton) layout.findViewById(R.id.btnCancel);
		final IjoomerRadioButton rdbSelectAll = (IjoomerRadioButton) layout.findViewById(R.id.rdbSelectAll);
		final IjoomerRadioButton rdbSelectNone = (IjoomerRadioButton) layout.findViewById(R.id.rdbSelectNone);
		IjoomerButton btnDone = (IjoomerButton) layout.findViewById(R.id.btnDone);

		if (selected != null && selected.size() > 0) {
			adapter = getListAdapter(prepareList(selected));
			lstContact.setAdapter(adapter);
			int count = 0;

			for (HashMap<String, Object> hashMap : selected) {
				if (hashMap.get("isChecked").equals("true")) {
					count++;
				}
			}
			if (count == selected.size()) {
				rdbSelectAll.setChecked(true);
				rdbSelectNone.setChecked(false);
			} else if (count == 0) {
				rdbSelectNone.setChecked(true);
				rdbSelectAll.setChecked(false);
			}

		} else {
			adapter = getListAdapter(prepareList(getContacts()));
			lstContact.setAdapter(adapter);
		}

		btnDone.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				String selectedString = "";
				ArrayList<HashMap<String, Object>> newSelectedList = new ArrayList<HashMap<String, Object>>();
				try {
					for (SmartListItem item : adapter.getSmartListItems()) {
						HashMap<String, Object> row = (HashMap<String, Object>) item.getValues().get(0);
						if (row.get("isChecked").toString().equals("true")) {
							selectedString += row.get("email").toString().split(";")[0] + ",";
						}
						newSelectedList.add(row);
					}
				} catch (Exception e) {
				}
				target.onClick("Email", newSelectedList, selectedString != null && selectedString.trim().length() > 0 ? selectedString.substring(0, selectedString.length() - 1)
						.trim() : "");
				popup.dismiss();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popup.dismiss();
			}
		});

		rdbSelectAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				rdbSelectNone.setChecked(false);
				int size = lstContact.getAdapter().getCount();
				for (int i = 0; i < size; i++) {
					((HashMap<String, String>) ((SmartListItem) lstContact.getAdapter().getItem(i)).getValues().get(0)).put("isChecked", "true");
				}
				((SmartListAdapterWithHolder) lstContact.getAdapter()).notifyDataSetChanged();
			}
		});
		rdbSelectNone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rdbSelectAll.setChecked(false);
				int size = lstContact.getAdapter().getCount();
				for (int i = 0; i < size; i++) {
					((HashMap<String, String>) ((SmartListItem) lstContact.getAdapter().getItem(i)).getValues().get(0)).put("isChecked", "false");
				}
				((SmartListAdapterWithHolder) lstContact.getAdapter()).notifyDataSetChanged();
			}
		});
	}

	private static SmartListAdapterWithHolder getListAdapter(final ArrayList<SmartListItem> data) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(mSmartIphoneActivity, R.layout.ijoomer_contact_mail_dialog_item, data, new ItemView() {
			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, final SmartListItem item, final ViewHolder holder) {

				holder.txtContactName = (IjoomerTextView) v.findViewById(R.id.txtContactName);
				holder.imgContactUser = (ImageView) v.findViewById(R.id.imgContactUser);
				holder.txtContactEmail = (IjoomerTextView) v.findViewById(R.id.txtContactEmail);
				holder.chbContact = (IjoomerCheckBox) v.findViewById(R.id.chbContact);

				final HashMap<String, Object> row = (HashMap<String, Object>) item.getValues().get(0);
				holder.chbContact.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton ButtonView, boolean isChecked) {
						row.put("isChecked", "" + isChecked);
					}
				});

				if (row.get("isChecked").toString().equals("false")) {
					holder.chbContact.setChecked(false);
				} else {
					holder.chbContact.setChecked(true);
				}
				try {
					holder.imgContactUser.setImageBitmap((Bitmap) (row).get("photo"));
				} catch (Throwable e) {
					holder.imgContactUser.setImageResource(R.drawable.ic_launcher);
				}
				holder.txtContactName.setText((row).get("name").toString());
				String[] emailArray = (row).get("email").toString().split(";");
				holder.txtContactEmail.setText(emailArray[0]);

				if (emailArray.length == 0) {
					holder.chbContact.setVisibility(View.GONE);
				}

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;
	}

	private static ArrayList<SmartListItem> prepareList(ArrayList<HashMap<String, Object>> data) {
		ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
		if (data != null) {
			int size = data.size();
			for (int i = 0; i < size; i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.ijoomer_contact_mail_dialog_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(data.get(i));
				item.setValues(obj);
				listData.add(item);
			}
		}
		return listData;
	}

	/**
	 * This method will generate and show the OK/Cancel dialog with given
	 * message and single message IjoomerButton.<br>
	 * 
	 * @param title
	 *            = String title will be the title of OK dialog.
	 * @param msg
	 *            = String msg will be the message in OK dialog.
	 * @param positiveBtnCaption
	 *            = String positiveBtnCaption will be the name of OK
	 *            IjoomerButton.
	 * @param negativeBtnCaption
	 *            = String negativeBtnCaption will be the name of OK
	 *            IjoomerButton.
	 * @param target
	 *            = String target is AlertNewtral callback for OK IjoomerButton
	 *            click action.
	 */
	public static void getConfirmDialog(String title, String msg, String positiveBtnCaption, String negativeBtnCaption, boolean isCancelable, final AlertMagnatic target) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mSmartIphoneActivity);

		int imageResource = android.R.drawable.ic_dialog_alert;
		Drawable image = mSmartIphoneActivity.getResources().getDrawable(imageResource);

		builder.setTitle(title).setMessage(msg).setIcon(image).setCancelable(false).setPositiveButton(positiveBtnCaption, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				target.PositiveMathod(dialog, id);
			}
		}).setNegativeButton(negativeBtnCaption, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				target.NegativeMathod(dialog, id);
			}
		});

		AlertDialog alert = builder.create();
		alert.setCancelable(isCancelable);
		alert.show();
	}

	/**
	 * This Method check whether Internet connection is available or not.
	 * 
	 * @return <b>true</b> if actual network connection is there, otherwise
	 *         <b>false.
	 */
	public static boolean isNetwokReachable() {
		final ConnectivityManager connMgr = (ConnectivityManager) mSmartIphoneActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnected()) {
			try {
				return true;
				// URL url = new URL("http://www.google.com");
				// HttpURLConnection urlc = (HttpURLConnection)
				// url.openConnection();
				// urlc.setRequestProperty("User-Agent", "Android Application");
				// urlc.setRequestProperty("Connection", "close");
				// urlc.setConnectTimeout(10 * 1000);
				// urlc.connect();
				// if (urlc.getResponseCode() == 200) {
				// return true;
				// } else {
				// return false;
				// }
			} catch (Throwable e) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static void finishActivity() {
		if (mSmartIphoneActivity != null)
			mSmartIphoneActivity.finish();
	}

	public static void getMultiSelectionDialog(final String name, String jsonString, final String value, final CustomClickListner target) {

		final ArrayList<String> values = new ArrayList<String>();

		final boolean[] selections = new boolean[values.size()];

		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				values.add(((JSONObject) jsonArray.get(i)).getString("value"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		final StringBuilder newValue = new StringBuilder();

		AlertDialog alert = null;

		if (value.length() > 0) {
			String[] oldValue = value.split(",");
			int size = values.size();
			for (int i = 0; i < size; i++) {
				int len = oldValue.length;
				for (int j = 0; j < size; j++) {
					if (values.get(i).toString().equalsIgnoreCase(oldValue[j])) {
						selections[i] = true;
						break;
					}
				}

			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(mSmartIphoneActivity);
		builder.setTitle(name);
		builder.setMultiChoiceItems(values.toArray(new CharSequence[values.size()]), selections, new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				selections[which] = isChecked;
			}
		});

		builder.setPositiveButton(mSmartIphoneActivity.getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int size = selections.length;
				for (int i = 0; i < size; i++) {
					if (selections[i]) {
						newValue.append(newValue.length() > 0 ? ", " + values.get(i) : values.get(i));
					}
				}
				target.onClick(newValue.toString());

			}
		});
		builder.setNegativeButton(mSmartIphoneActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				target.onClick(value);
			}
		});
		alert = builder.create();
		alert.show();

	}

	public static void getDateDialog(final String strDate, boolean restrict, final CustomClickListner target) {

		Date date = getDateFromString(strDate);
		Calendar c = GregorianCalendar.getInstance();

		if (date.getYear() == new Date().getYear() && restrict) {
			c.add(Calendar.YEAR, -18);
		} else {
			c.set(Calendar.YEAR, (date.getYear() + 1900));
			c.set(Calendar.MONTH, date.getMonth());
			c.set(Calendar.DATE, date.getDate());
		}

		IjoomerDataPickerView dateDlg = new IjoomerDataPickerView(mSmartIphoneActivity, new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Time chosenDate = new Time();
				chosenDate.set(dayOfMonth, monthOfYear, year);
				long dt = chosenDate.toMillis(true);
				CharSequence strDate = DateFormat.format(IjoomerApplicationConfiguration.dateFormat, dt);
				target.onClick(strDate.toString());

			}

		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), restrict);

		dateDlg.show();

	}

	public static void getDateTimeDialog(final String strDate, final CustomClickListner target) {
		final Date date = getDateFromString(strDate);
		DatePickerDialog dateDialog = new DatePickerDialog(mSmartIphoneActivity, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				final int y = year;
				final int m = monthOfYear;
				final int d = dayOfMonth;

				new TimePickerDialog(mSmartIphoneActivity, new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						Time chosenDate = new Time();
						chosenDate.set(0, minute, hourOfDay, d, m, y);
						long dt = chosenDate.toMillis(true);
						CharSequence strDate = DateFormat.format(IjoomerApplicationConfiguration.dateTimeFormat, dt);
						target.onClick(strDate.toString());
					}
				}, date.getHours(), date.getMinutes(), true).show();

			}
		}, date.getYear() + 1900, date.getMonth(), date.getDate());

		dateDialog.show();

	}

	public static void getTimeDialog(final int hour, final int min, final CustomClickListner target) {

		TimePickerDialog timeDialog = new TimePickerDialog(mSmartIphoneActivity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				SimpleDateFormat fmt = new SimpleDateFormat(IjoomerApplicationConfiguration.timeFormat);
				Date date = new Date();
				date.setHours(hourOfDay);
				date.setMinutes(minute);
				String dateString = fmt.format(date);
				target.onClick(dateString);
			}
		}, hour == 0 ? Calendar.getInstance().get(Calendar.HOUR) : hour, min == 0 ? Calendar.getInstance().get(Calendar.MINUTE) : min, true);

		timeDialog.show();

	}

	public static MyCustomAdapter getSpinnerAdapter(HashMap<String, String> field) {

		int index = 0;
		final ArrayList<String> values = new ArrayList<String>();

		try {

			JSONArray jsonArray = new JSONArray(field.get("options"));
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject options = (JSONObject) jsonArray.get(i);

				if (options.has("title")) {
					values.add(options.getString("title"));
				} else if (options.has("name")) {
					values.add(options.getString("name").replace("&rsaquo;", " > "));
				} else if (options.has("caption")) {
					values.add(options.getString("caption"));
				} else {
					values.add(options.getString("value"));
				}
				if (options.getString("value").equals(field.get("value")) || options.has("title") && options.getString("title").equals(field.get("value")) || options.has("name")
						&& options.getString("name").equals(field.get("value"))) {
					index = i;
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
		final MyCustomAdapter adpater = new MyCustomAdapter(mSmartIphoneActivity, values);
		adpater.setDefaultPostion(index);
		return adpater;

	}

	public static MyCustomAdapter getPrivacySpinnerAdapter(HashMap<String, String> field) {

		int index = 0;
		final ArrayList<String> values = new ArrayList<String>();
		JSONArray jsonArray;

		try {
			jsonArray = new JSONObject(field.get("privacy")).getJSONArray("options");
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject options = (JSONObject) jsonArray.get(i);

				if (options.has("title")) {
					values.add(options.getString("title"));
				} else if (options.has("name")) {
					values.add(options.getString("name").replace("&rsaquo;", " > "));
				} else if (options.has("caption")) {
					values.add(options.getString("caption"));
				} else {
					values.add(options.getString("value"));
				}

				if (options.getString("value").equals(new JSONObject(field.get("privacy")).getString("value"))) {
					index = i;
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
		MyCustomAdapter adpater = new MyCustomAdapter(mSmartIphoneActivity, values);
		adpater.setDefaultPostion(index);
		return adpater;

	}

	public static class MyCustomAdapter extends ArrayAdapter<String> {

		Context context;
		ArrayList<String> list;
		private int defaultPosition;

		public int getDefaultPosition() {
			return defaultPosition;
		}

		public MyCustomAdapter(Context context, ArrayList<String> objects) {
			super(context, 0, objects);
			this.context = context;
			list = objects;
		}

		public void setDefaultPostion(int position) {
			this.defaultPosition = position;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustom(position, convertView, parent);
		}

		public View getCustom(int position, View convertView, ViewGroup parent) {

			View row = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
			TextView label = (TextView) row.findViewById(android.R.id.text1);
			label.setText(list.get(position));

			return row;
		}

		public View getCustomView(int position, View convertView, ViewGroup parent) {

			View row = LayoutInflater.from(context).inflate(R.layout.ijoomer_spinner_item, parent, false);
			IjoomerTextView label = (IjoomerTextView) row.findViewById(R.id.text1);
			label.setText(list.get(position));

			return row;
		}
	}

	public static boolean emailValidator(final String mailAddress) {

		Pattern pattern;
		Matcher matcher;

		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(mailAddress);
		return matcher.matches();

	}

	public static boolean birthdateValidator(String birthDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(IjoomerApplicationConfiguration.dateFormat);
		try {
			Date bdate = dateFormat.parse(birthDate);
			Date today = new Date();
			bdate = new GregorianCalendar(bdate.getYear() + 1900, bdate.getMonth() + 1, bdate.getDate(), 0, 0).getTime();
			today = new GregorianCalendar(today.getYear() + 1900, today.getMonth() + 1, today.getDate(), 0, 0).getTime();

			if (bdate.getTime() >= today.getTime()) {
				return false;
			} else {
				return true;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Date getDateFromString(String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(IjoomerApplicationConfiguration.dateFormat);
		Date date;
		try {
			date = dateFormat.parse(strDate);
			return date;
		} catch (Throwable e) {
			e.printStackTrace();
			return new Date();
		}
	}

	public static void selectImageDialog(final SelectImageDialogListner target) {
		final Dialog dialog = new Dialog(mSmartIphoneActivity, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.ijoomer_select_image_dialog);
		final IjoomerTextView txtCapture = (IjoomerTextView) dialog.findViewById(R.id.txtCapture);
		final IjoomerTextView txtPhoneGallery = (IjoomerTextView) dialog.findViewById(R.id.txtPhoneGallery);
		txtCapture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				target.onCapture();
				dialog.dismiss();

			}
		});
		txtPhoneGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				target.onPhoneGallery();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public static class RichTextUtils {
		public static <A extends CharacterStyle, B extends CharacterStyle> Spannable replaceAll(Spanned original, Class<A> sourceType, SpanConverter<A, B> converter) {
			SpannableStringBuilder result = new SpannableStringBuilder(original);
			A[] spans = result.getSpans(0, result.length(), sourceType);

			for (A span : spans) {
				int start = result.getSpanStart(span);
				int end = result.getSpanEnd(span);
				int flags = result.getSpanFlags(span);

				result.removeSpan(span);
				result.setSpan(converter.convert(span), start, end, flags);
			}

			return (result);
		}

		public interface SpanConverter<A extends CharacterStyle, B extends CharacterStyle> {
			B convert(A span);
		}
	}

	public static class URLSpanConverter implements RichTextUtils.SpanConverter<URLSpan, CustumClicableSpan> {

		@Override
		public CustumClicableSpan convert(URLSpan span) {
			return new CustumClicableSpan(span.getURL());
		}
	}

	private static class CustumClicableSpan extends ClickableSpan {
		private String url = "";

		public CustumClicableSpan(String url) {
			this.url = url;
		}

		@Override
		public void onClick(View widget) {
			System.out.println("Url :" + url);
			Intent intent = new Intent(mSmartIphoneActivity, IjoomerWebviewClient.class);
			intent.putExtra("url", url);
			mSmartIphoneActivity.startActivity(intent);

		}

	}

	public static ArrayList<HashMap<String, String>> getListIndexedData(ArrayList<HashMap<String, String>> oldData, String indexOn) {

		ArrayList<HashMap<String, String>> newData = new ArrayList<HashMap<String, String>>();

		String idx1 = null;
		String idx2 = null;

		for (HashMap<String, String> data : oldData) {
			idx1 = (data.get(indexOn).substring(0, 1)).toLowerCase();
			if (!idx1.equalsIgnoreCase(idx2)) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("listindexheader", idx1.toUpperCase());
				newData.add(map);
				idx2 = idx1;
			}
			newData.add(data);
		}
		return newData;
	}

	public static Address getLatLongFromAddress(String address) {
		if (address != null && address.length() > 0) {
			geocoder = new Geocoder(mSmartIphoneActivity);

			List<Address> list = null;
			try {
				list = geocoder.getFromLocationName(address, 1);
				return list.get(0);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public static Address getAddressFromLatLong(double lat, double lng) {

		if (lat == 0 || lng == 0) {
			lat = Double.parseDouble(mSmartIphoneActivity.getLatitude());
			lng = Double.parseDouble(mSmartIphoneActivity.getLongitude());
		}
		geocoder = new Geocoder(mSmartIphoneActivity);

		List<Address> list = null;
		try {
			list = geocoder.getFromLocation(lat, lng, 10);
			return list.get(0);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Address> getAddressListFromLatLong(double lat, double lng) {

		if (lat == 0 || lng == 0) {
			lat = Double.parseDouble(mSmartIphoneActivity.getLatitude());
			lng = Double.parseDouble(mSmartIphoneActivity.getLongitude());
		}
		geocoder = new Geocoder(mSmartIphoneActivity);

		List<Address> list = null;
		try {
			list = geocoder.getFromLocation(lat, lng, 10);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<HashMap<String, String>> getContatctFromDevice(String... args) {
		Cursor cursor = null;
		// String[] projection = new
		// String[]{RawContacts.CONTACT_ID,Contacts.DISPLAY_NAME};
		try {
			cursor = mSmartIphoneActivity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				final String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
				final String contactId = cursor.getString(cursor.getColumnIndex(RawContacts.CONTACT_ID));
				// final String number =
				// cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				// final String email =
				// cursor.getString(cursor.getColumnIndex(Email.DATA));
				System.out.println(name);
				System.out.println(contactId);
				// System.out.println(email);
				cursor.moveToNext();
			}

		}
		return null;
	}

	@SuppressWarnings("serial")
	public static ArrayList<HashMap<String, Object>> getContacts() {

		ArrayList<HashMap<String, Object>> contacts = new ArrayList<HashMap<String, Object>>();
		final String[] projection = new String[] { RawContacts.CONTACT_ID, RawContacts.DELETED };

		final Cursor rawContacts = mSmartIphoneActivity.getContentResolver().query(RawContacts.CONTENT_URI, projection, null, null, null);

		final int contactIdColumnIndex = rawContacts.getColumnIndex(RawContacts.CONTACT_ID);
		final int deletedColumnIndex = rawContacts.getColumnIndex(RawContacts.DELETED);

		if (rawContacts.moveToFirst()) {
			while (!rawContacts.isAfterLast()) {
				final int contactId = rawContacts.getInt(contactIdColumnIndex);
				final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);

				if (!deleted) {
					HashMap<String, Object> contactInfo = new HashMap<String, Object>() {
						{
							put("contactId", "");
							put("name", "");
							put("email", "");
							put("address", "");
							put("photo", "");
							put("phone", "");
						}
					};
					contactInfo.put("contactId", "" + contactId);
					contactInfo.put("name", getName(contactId));
					contactInfo.put("email", getEmail(contactId));
					contactInfo.put("photo", getPhoto(contactId) != null ? getPhoto(contactId) : "");
					contactInfo.put("address", getAddress(contactId));
					contactInfo.put("phone", getPhoneNumber(contactId));
					contactInfo.put("isChecked", "false");
					contacts.add(contactInfo);
				}
				rawContacts.moveToNext();
			}
		}

		rawContacts.close();

		return contacts;
	}

	private static String getName(int contactId) {
		String name = "";
		final String[] projection = new String[] { Contacts.DISPLAY_NAME };

		final Cursor contact = mSmartIphoneActivity.getContentResolver().query(Contacts.CONTENT_URI, projection, Contacts._ID + "=?", new String[] { String.valueOf(contactId) },
				null);

		if (contact.moveToFirst()) {
			name = contact.getString(contact.getColumnIndex(Contacts.DISPLAY_NAME));
			contact.close();
		}
		contact.close();
		return name;

	}

	private static String getEmail(int contactId) {
		String emailStr = "";
		final String[] projection = new String[] { Email.DATA, // use
				// Email.ADDRESS
				// for API-Level
				// 11+
				Email.TYPE };

		final Cursor email = mSmartIphoneActivity.getContentResolver().query(Email.CONTENT_URI, projection, Data.CONTACT_ID + "=?", new String[] { String.valueOf(contactId) },
				null);

		if (email.moveToFirst()) {
			final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);

			while (!email.isAfterLast()) {
				emailStr = emailStr + email.getString(contactEmailColumnIndex) + ";";
				email.moveToNext();
			}
		}
		email.close();
		return emailStr;

	}

	private static Bitmap getPhoto(int contactId) {
		Bitmap photo = null;
		final String[] projection = new String[] { Contacts.PHOTO_ID };

		final Cursor contact = mSmartIphoneActivity.getContentResolver().query(Contacts.CONTENT_URI, projection, Contacts._ID + "=?", new String[] { String.valueOf(contactId) },
				null);

		if (contact.moveToFirst()) {
			final String photoId = contact.getString(contact.getColumnIndex(Contacts.PHOTO_ID));
			if (photoId != null) {
				photo = getBitmap(photoId);
			} else {
				photo = null;
			}
		}
		contact.close();

		return photo;
	}

	private static Bitmap getBitmap(String photoId) {
		final Cursor photo = mSmartIphoneActivity.getContentResolver().query(Data.CONTENT_URI, new String[] { Photo.PHOTO }, Data._ID + "=?", new String[] { photoId }, null);

		final Bitmap photoBitmap;
		if (photo.moveToFirst()) {
			byte[] photoBlob = photo.getBlob(photo.getColumnIndex(Photo.PHOTO));
			photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.length);
		} else {
			photoBitmap = null;
		}
		photo.close();
		return photoBitmap;
	}

	private static String getAddress(int contactId) {
		String postalData = "";
		String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] addrWhereParams = new String[] { String.valueOf(contactId), ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };

		Cursor addrCur = mSmartIphoneActivity.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, addrWhere, addrWhereParams, null);

		if (addrCur.moveToFirst()) {
			postalData = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
		}
		addrCur.close();
		return postalData;
	}

	private static String getPhoneNumber(int contactId) {

		String phoneNumber = "";
		final String[] projection = new String[] { Phone.NUMBER, Phone.TYPE, };
		final Cursor phone = mSmartIphoneActivity.getContentResolver().query(Phone.CONTENT_URI, projection, Data.CONTACT_ID + "=?", new String[] { String.valueOf(contactId) },
				null);

		if (phone.moveToFirst()) {
			final int contactNumberColumnIndex = phone.getColumnIndex(Phone.DATA);

			while (!phone.isAfterLast()) {
				phoneNumber = phoneNumber + phone.getString(contactNumberColumnIndex) + ";";
				phone.moveToNext();
			}

		}
		phone.close();
		return phoneNumber;
	}

	public String getReportCode(String privacy) {

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(mSmartIphoneActivity.getResources().getStringArray(R.array.report_type)));

		if (privacy.equals(list.get(0))) {
			return "0";
		} else if (privacy.equals(list.get(1))) {
			return "1";
		} else if (privacy.equals(list.get(2))) {
			return "2";
		}
		return "0";
	}

	public String getPrivacyString(String privacy) {

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(mSmartIphoneActivity.getResources().getStringArray(R.array.report_type)));

		if (privacy.equals("0")) {
			return list.get(0);
		} else if (privacy.equals("1")) {
			return list.get(1);
		} else if (privacy.equals("2")) {
			return list.get(2);
		}
		return list.get(0);
	}

	public int getPrivacyIndex(String privacy) {

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(mSmartIphoneActivity.getResources().getStringArray(R.array.report_type)));

		if (privacy.equals("0") || privacy.equals(list.get(0))) {
			return 0;
		} else if (privacy.equals("1") || privacy.equals(list.get(1))) {
			return 1;
		} else if (privacy.equals("2") || privacy.equals(list.get(2))) {
			return 2;
		}
		return 0;
	}

	public static void IjoomerTextViewResizable(final IjoomerTextView tv, final int maxLine, final String expandText) {

		if (tv.getTag() == null) {
			tv.setTag(tv.getText());
		}
		ViewTreeObserver vto = tv.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				ViewTreeObserver obs = tv.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				if (maxLine <= 0) {
					int lineEndIndex = tv.getLayout().getLineEnd(0);
					String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
					tv.setText(text);
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					tv.setText(addClickablePartIjoomerTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText), BufferType.SPANNABLE);
				} else if (tv.getLineCount() >= maxLine) {
					int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
					String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
					tv.setText(text);
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					tv.setText(addClickablePartIjoomerTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText), BufferType.SPANNABLE);
				}
			}
		});

	}

	private static SpannableStringBuilder addClickablePartIjoomerTextViewResizable(final Spanned strSpanned, final IjoomerTextView tv, final int maxLine, final String expandText) {
		String str = strSpanned.toString();
		SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

		if (str.contains(expandText)) {
			ssb.setSpan(new IjoomerSpannable(Color.parseColor(mSmartIphoneActivity.getString(R.color.blue)), true) {

				@Override
				public void onClick(View widget) {
					tv.setLayoutParams(tv.getLayoutParams());
					tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
					tv.invalidate();
				}

			}, str.indexOf(expandText), str.indexOf(expandText) + expandText.length(), 0);

		}
		return ssb;

	}

	public static String getDateStringCurrentTimeZone(long timestamp) {

		Calendar calendar = Calendar.getInstance();
		TimeZone t = TimeZone.getTimeZone(IjoomerGlobalConfiguration.getServerTimeZone());

		calendar.setTimeInMillis(timestamp * 1000);
		calendar.add(Calendar.MILLISECOND, t.getOffset(calendar.getTimeInMillis()));

		SimpleDateFormat sdf = new SimpleDateFormat(IjoomerApplicationConfiguration.dateTimeFormat);
		String dateString = sdf.format(calendar.getTime());
		return dateString;
	}

	public static Date getDateCurrentTimeZone(long timestamp) {

		Calendar calendar = Calendar.getInstance();
		TimeZone t = TimeZone.getTimeZone(IjoomerGlobalConfiguration.getServerTimeZone());

		calendar.setTimeInMillis(timestamp * 1000);
		calendar.add(Calendar.MILLISECOND, t.getOffset(calendar.getTimeInMillis()));

		return (Date) calendar.getTime();
	}

	public static long getMillisecondsTimeZone(long timestamp) {

		Calendar calendar = Calendar.getInstance();
		TimeZone t = TimeZone.getTimeZone(IjoomerGlobalConfiguration.getServerTimeZone());

		calendar.setTimeInMillis(timestamp * 1000);
		calendar.add(Calendar.MILLISECOND, t.getOffset(calendar.getTimeInMillis()));
		System.out.println("Date : " + calendar.getTime());
		return calendar.getTimeInMillis();
	}

	public static long getDfferenceInMinute(long miliseconds) {
		long diff = (Calendar.getInstance().getTimeInMillis() - miliseconds);
		diff = diff / 60000L;
		return Math.abs(diff);
	}

	public static String calculateTimesAgo(long miliseconds) {
		Date start = new Date(miliseconds);
		Date end = new Date();

		long diffInSeconds = (end.getTime() - start.getTime()) / 1000;

		long diff[] = new long[] { 0, 0, 0, 0 };
		/* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
		/* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
		/* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
		/* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));

		System.out.println(String.format("%d day%s, %d hour%s, %d minute%s, %d second%s ago", diff[0], diff[0] > 1 ? "s" : "", diff[1], diff[1] > 1 ? "s" : "", diff[2],
				diff[2] > 1 ? "s" : "", diff[3], diff[3] > 1 ? "s" : ""));

		if (diff[0] > 0) {
			Calendar c = Calendar.getInstance();
			c.setTime(start);

			if (c.getMaximum(Calendar.DATE) <= diff[0]) {
				return start.toLocaleString();
			} else {
				return diff[0] > 1 ? String.format("%d days ago", diff[0]) : String.format("%d day ago", diff[0]);
			}
		} else if (diff[1] > 0) {
			return diff[1] > 1 ? String.format("%d hours ago", diff[1]) : String.format("%d hour ago", diff[1]);
		} else if (diff[2] > 0) {
			return diff[2] > 1 ? String.format("%d minutes ago", diff[2]) : String.format("%d minute ago", diff[2]);
		} else if (diff[3] > 0) {
			return diff[3] > 1 ? String.format("%d seconds ago", diff[3]) : String.format("%d second ago", diff[3]);
		} else {
			return start.toLocaleString();
		}

	}

	public static JSONObject getLoginParams() {
		JSONObject loginParams = null;
		try {
			loginParams = new JSONObject(((SmartActivity) mSmartIphoneActivity).getSmartApplication().readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, ""));
		} catch (Exception e) {
		}
		return loginParams;
	}

	public static String prepareEmailBody(String... parms) {

		String data = "";
		if (parms != null && parms.length > 0) {
			for (String row : parms) {
				if (row.contains("http") || row.contains("https")) {
					row = "<a href='" + row + "'> " + row + " </a>";
				}
				data += "<br/> <br/>" + row;
			}
		}

		return data;
	}

	public static String readableFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
