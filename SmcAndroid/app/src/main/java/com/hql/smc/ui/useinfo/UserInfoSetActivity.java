package com.hql.smc.ui.useinfo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.activity.NoMvpActivity;
import com.hql.smc.data.livedata.UserData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserInfoSetActivity extends NoMvpActivity {
    private TextView nickname;
    private TextView email;
    private TextView des;
    private RadioButton male;
    private RadioButton female;
    private DatePicker birthday;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info_set;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        click(R.id.back, this::finish);
        click(R.id.save, this::save);
        nickname = get(R.id.nickname);
        email = get(R.id.email);
        des = get(R.id.des);
        male = get(R.id.male);
        female = get(R.id.female);
        birthday = get(R.id.birthday);
        UserData.getInstance().observe(this, user -> {
            nickname.setText(user.getNickname());
            email.setText(user.getEmail());
            des.setText(user.getDes());
            if (user.getGender() == 1) {
                male.setChecked(true);
            } else {
                female.setChecked(true);
            }
            try {
                @SuppressLint("SimpleDateFormat")
                Date date = new SimpleDateFormat("yyyyMMdd").parse(user.getBirthday());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                birthday.init(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        null
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private void save() {
        String nickname = this.nickname.getText().toString();
        String email = this.email.getText().toString();
        String des = this.des.getText().toString();
        int gender = male.isChecked() ? 1 : 2;
        @SuppressLint("DefaultLocale")
        String birthday = String.format("%d%02d%d",
                this.birthday.getYear(),
                this.birthday.getMonth() + 1,
                this.birthday.getDayOfMonth()
        );
        Api.setUserInfo(nickname, gender, birthday, email, des)
                .error((message, e) -> toast(message))
                .success(data -> {
                    UserData.getInstance().postValue(data);
                    toast("修改成功");
                })
                .run();
    }
}
