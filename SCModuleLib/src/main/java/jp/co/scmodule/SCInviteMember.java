package jp.co.scmodule;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCGroupObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;

public class SCInviteMember extends SCMyActivity {
    private static final int CONTACT_PICKER_RESULT = 2;
    private static final int SEND_MAIL_RESULT = 3;
    private TextView btn_skip = null;
    private Button submit = null;
    private ImageView back = null;
    private TextView contact = null;
    private EditText et_email = null;
    private View.OnClickListener mOnClickListener = null;
    private SCGroupObject groupObject = null;
    private String type = "";
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scinvite_member);
        SCGlobalUtils.sActivityArr.add(this);
        mContext = this;
        try {
            groupObject = getIntent().getParcelableExtra(SCGroupObject.class.toString());
            type = getIntent().getStringExtra("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    protected void onRestart() {
        initGlobalUtils();
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        afterClickBack();
        super.onBackPressed();
    }

    private void afterClickBack() {
        if (type == null) {
            Intent intent = new Intent(SCInviteMember.this, SCAddGroup.class);
            intent.putExtra("group_id",
                    groupObject.getGroup_id());
            startActivity(intent);
        }else if(type.equals("")){
            Intent intent = new Intent(SCInviteMember.this, SCAddGroup.class);
            intent.putExtra("group_id",
                    groupObject.getGroup_id());
            startActivity(intent);
        }

        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }

    @Override
    protected void findViewById() {
        btn_skip = (TextView) findViewById(R.id.btn_skip);
        submit = (Button) findViewById(R.id.btn_submit);
        back = (ImageView) findViewById(R.id.img_left_header);
        contact = (TextView) findViewById(R.id.btn_contact);
        et_email = (EditText) findViewById(R.id.edit_info_one_btn_group);
        if (type != null && !type.equals("")) {
            btn_skip.setVisibility(View.GONE);
        }

        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            setUpViewsForTadacopy();
        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            setUpViewsForCanpass();
        }
        else if (getPackageName().equals(SCConstants.PACKAGE_TORETAN_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TORETAN_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TORETAN_STAGING)) {
            setUpViewsForToretan();
        }
    }

    private void setUpViewsForTadacopy() {
    }

    private void setUpViewsForCanpass() {
        submit.setBackgroundResource(R.drawable.selector_btn_next_canpass);
        back.setImageResource(R.drawable.yellow_left_arrow_canpass);

        btn_skip.setTextColor(getResources().getColor(R.color.canpass_main));
        contact.setTextColor(getResources().getColor(R.color.canpass_main));

    }
    private void setUpViewsForToretan() {
        submit.setBackgroundResource(R.drawable.selector_btn_next_toretan);
        back.setImageResource(R.drawable.blue_left_arrow_toretan);

        et_email.setBackgroundResource(R.drawable.edittextbackground);

        btn_skip.setTextColor(getResources().getColor(R.color.toretan_main));
        contact.setTextColor(getResources().getColor(R.color.toretan_main));

    }

    @Override
    protected void initListeners() {
        btn_skip.setContentDescription("skip");
        submit.setContentDescription("submit");
        back.setContentDescription("back");
        contact.setContentDescription("contact");

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("skip")) {
                    afterClickSkip();
                } else if (v.getContentDescription().equals("submit")) {
                    String dialogBody = null;
                    String mail = et_email.getText().toString();

                    if (mail.equals("")) {
                        dialogBody = getResources().getString(R.string.dialog_body_email_empty_label);
                        SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
                        return;
                    }  else if (!SCGlobalUtils.isEmailValid(mail)) {
                        dialogBody = getResources().getString(R.string.dialog_body_email_invalid_label);
                        SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
                        return;
                    }
                    afterClickSubmit();
                } else if (v.getContentDescription().equals("back")) {
                    afterClickBack();
                } else if (v.getContentDescription().equals("contact")) {
                    afterClickContact();
                }
            }
        };
    }

    private void afterClickSubmit() {
        SCUserObject userObject = SCUserObject.getInstance();

        final String subject = userObject.getNickname() +" さんから "+userObject.getStudent_group_name()+" への招待が届いています！";

        final String body = userObject.getNickname() + " さんから " + userObject.getStudent_group_name() + " への招待が届いています！今すぐ登録してCirclePassを始めましょう！\n" +
                "\n" +
                "登録用アプリ【タダコピ】\n" +
                "http://app.tadacopy.jp/app_users/install\n" +
                "\n" +
                "CirclePassは学生団体やサークル活動を応援するポイント交換サービスです。CirclePassに登録すると、学生団体向けの様々なサービスを利用できるようになります。\n" +
                "\n" +
                "例えば、\n" +
                "・飲み会の割引券がもらえる！\n" +
                "・名刺やチラシ、冊子の印刷がタダになる！\n" +
                "・デザインソフトやMac完備の作業室が使える！\n" +
                "・登録団体同士の交流イベントに参加できる！\n" +
                "・焼肉などの食事会に無料招待！\n" +
                "他にも特典がいっぱい！\n" +
                "\n" +
                "詳細はこちらから\n" +
                "http://cp.smart-campus.jp\n" +
                "\n" +
                "CirclePassに登録してサークル活動をもっと自由に。";


        final Intent result = new Intent(android.content.Intent.ACTION_SEND);
        result.setType("plain/text");
        result.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{et_email.getText().toString().trim()});
        result.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        result.putExtra(android.content.Intent.EXTRA_TEXT, body);
        result.putExtra(Intent.EXTRA_BCC, new String[]{"support@smart-campus.jp"});
        startActivityForResult(Intent.createChooser(result, "Chooser Title"), SEND_MAIL_RESULT);


    }

    private void afterClickContact() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    private void afterClickSkip() {
        Intent intent = new Intent(SCInviteMember.this, SCGroupProfile.class);
        intent.putExtra("group_id",
                groupObject.getGroup_id());
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    @Override
    protected void setListenerForViews() {
        btn_skip.setOnClickListener(mOnClickListener);
        submit.setOnClickListener(mOnClickListener);
        back.setOnClickListener(mOnClickListener);
        contact.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void resizeScreen() {
        new SCMultipleScreen(this);
        SCMultipleScreen.resizeAllView(this);
    }

    @Override
    protected void initGlobalUtils() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEND_MAIL_RESULT) {
            if (type != null && !type.equals("")) {

            } else {
                Intent intent = new Intent(SCInviteMember.this, SCGroupProfile.class);
                intent.putExtra("group_id",
                        groupObject.getGroup_id());
                startActivity(intent);
            }
            this.finish();
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);

        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String email = "", name = "";
                    try {
                        Uri result = data.getData();
                        Log.e("ActivityResult", "Got a contact result: " + result.toString());

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();

                        // query for everything email
                        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[]{id}, null);

                        int nameId = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                        int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                        // let's just get the first email
                        if (cursor.moveToFirst()) {
                            email = cursor.getString(emailIdx);
                            name = cursor.getString(nameId);
                            Log.e("ActivityResult", "Got email: " + email);
                        } else {
                            Log.e("ActivityResult", "No results");
                        }
                    } catch (Exception e) {
                        Log.e("ActivityResult", "Failed to get email data", e);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }

                        et_email.setText(email);

                        if (email.length() == 0 && name.length() == 0) {
                            Toast.makeText(this, "No Email for Selected Contact", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;

            }


        } else {
            Log.e("ActivityResult", "Warning: activity result not ok");
        }

    }


}
