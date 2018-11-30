package com.adc2018.bpmhw3.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.entity.rmp.User;
import com.adc2018.bpmhw3.entity.rmp.UserCard;
import com.adc2018.bpmhw3.entity.rmp.list.UserCardList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.utils.BPM3;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DesignCardActivity extends AppCompatActivity {

    private static final String TAG = DesignCardActivity.class.getSimpleName();

    private EditText editName;
    private EditText editCompany;
    private EditText editPosition;
    private EditText editPhone;
    private EditText editAddress;
    private EditText editEmail;
    private User user;
    private Card card;
    private UserCard userCard;

    private BPMApi api = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);

    private void initActivity(UserCard userCard) {
        setContentView(R.layout.activity_design_card);
        editName = findViewById(R.id.editName);
        editCompany = findViewById(R.id.editCompany);
        editPosition = findViewById(R.id.editPosition);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        editEmail = findViewById(R.id.editEmail);
        this.userCard = userCard;
        if(userCard != null) {
            card = userCard.getCard();
            if(card != null) {
                editName.setText(card.getName());
                editCompany.setText(card.getCompany());
                editPosition.setText(card.getPosition());
                editPhone.setText(card.getPhone_number());
                editAddress.setText(card.getAddress());
                editEmail.setText(card.getEmail());
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressbar_layout);
        this.user = BPM3.user;
        Log.d(TAG, "onCreate: " + BPM3.user.toString());
        Call<UserCardList> call = api.getUserCardByUserId(BPM3.user.getId());
        call.enqueue(new Callback<UserCardList>() {
            @Override
            public void onResponse(Call<UserCardList> call, Response<UserCardList> response) {
                if(response.isSuccessful()) {
                    UserCardList userCardList = response.body();
                    if(userCardList.empty()) {
                        userCard = null;
                    }
                    else {
                        userCard = userCardList.getUsercard().get(0);
                    }
                    initActivity(userCard);
                }
                else {
                    Toast.makeText(DesignCardActivity.this, "请重试", Toast.LENGTH_SHORT).show();
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserCardList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }


    /**
     * 确认提交个人名片
     * @param view
     */
    public void confirmClick(View view) {
        if(userCard == null) {
            createCard();
        }
        else {
            updateUserCard();
        }
    }

    /**
     * 首次提交个人信息
     */
    public void createUserCard() {
        userCard = UserCard.Factory(user, card, null);
        Log.d(TAG, "createUserCard: " + userCard.toString());
        Call<UserCard> call = api.addUserCard(userCard);
        call.enqueue(new Callback<UserCard>() {
            @Override
            public void onResponse(Call<UserCard> call, Response<UserCard> response) {
                if(response.isSuccessful()) {
                    userCard = response.body();
                    Toast.makeText(DesignCardActivity.this, "名片创建成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DesignCardActivity.this, "名片创建失败", Toast.LENGTH_SHORT).show();
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                        userCard = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserCard> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }



    public void createCard() {
        final Card card = Card.CardFactory(editName.getText().toString(), editCompany.getText().toString(),
                editPosition.getText().toString(), editPhone.getText().toString(),
                editAddress.getText().toString(), editEmail.getText().toString());
        Call<Card> call = api.addCard(card);
        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if(response.isSuccessful()) {
                    DesignCardActivity.this.card = response.body();
                    createUserCard();
                }
                else {
                    Toast.makeText(DesignCardActivity.this, "名片创建失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {

            }
        });
    }

    /**
     * 修改个人信息
     */
    public void updateUserCard() {

    }
}
