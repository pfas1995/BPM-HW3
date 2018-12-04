package com.adc2018.bpmhw3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.adc2018.bpmhw3.R;
import com.adc2018.bpmhw3.entity.rmp.Card;
import com.adc2018.bpmhw3.entity.rmp.CardGroup;
import com.adc2018.bpmhw3.entity.rmp.UserGroup;
import com.adc2018.bpmhw3.entity.rmp.list.UserGroupList;
import com.adc2018.bpmhw3.network.RetrofitTool;
import com.adc2018.bpmhw3.network.api.rmp.BPMApi;
import com.adc2018.bpmhw3.network.api.rmp.RmpUtil;
import com.adc2018.bpmhw3.utils.BPM3;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdjustActivity extends AppCompatActivity {

    private static final String TAG = AdjustActivity.class.getSimpleName();

    private EditText editName;
    private EditText editCompany;
    private EditText editPosition;
    private EditText editPhone;
    private EditText editAddress;
    private EditText editEmail;
    private Spinner spinner;

    private BPMApi api = RetrofitTool.getRetrofit(RmpUtil.getBaseUrl()).create(BPMApi.class);

    private Card card;
    private UserGroup userGroup;
    private CardGroup defaultCardGroup;
    private List<CardGroup> cardGroups;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        init();
    }

    public void initWidegt() {
        setContentView(R.layout.activity_adjust);
        editName = findViewById(R.id.editName);
        editCompany = findViewById(R.id.editCompany);
        editPosition = findViewById(R.id.editPosition);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        editEmail = findViewById(R.id.editEmail);
        spinner = findViewById(R.id.groupChoose);
        card = (Card) getIntent().getSerializableExtra("card");
        editName.setText(card.getName());
        editCompany.setText(card.getCompany());
        editPosition.setText(card.getPosition());
        editPhone.setText(card.getPhone_number());
        editAddress.setText(card.getAddress());
        editEmail.setText(card.getEmail());
    }
    public void initSpinner() {

    }
    /**
     * 初始化，获取用户的默认名片分组
     *
     */
    private void init() {
        Call<UserGroupList> call = api.getUserGroupByUserId(BPM3.user.getId());
        call.enqueue(new Callback<UserGroupList>() {
            @Override
            public void onResponse(Call<UserGroupList> call, Response<UserGroupList> response) {
                if(response.isSuccessful()) {
                    UserGroupList userGroupList = response.body();
                    userGroup = userGroupList.getUserGgroup().get(0);
                    cardGroups = userGroup.getGroup();
                    defaultCardGroup = findDefaultGroup(cardGroups);
                    initWidegt();
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                        finish();
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserGroupList> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                finish();
            }
        });
    }

    public CardGroup findDefaultGroup(List<CardGroup> cardGroups) {
        for(CardGroup cardGroup : cardGroups) {
            if(cardGroup.getGroup_name().contains("默认分组")) {
                return cardGroup;
            }
        }
        return null;
    }


    /**
     * 取消按钮响应
     * @param view
     */
    public void cancleClick(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void addCard() {
        card = Card.CardFactory(editName.getText().toString(), editCompany.getText().toString(),
                editPosition.getText().toString(), editPhone.getText().toString(),
                editAddress.getText().toString(), editEmail.getText().toString());
        Call<Card> call = api.addCard(card);
        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if(response.isSuccessful()) {
                    card = response.body();
                    addCardToDefaultGroup();
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void addCardToDefaultGroup() {
        defaultCardGroup.addCard(card);
        Call<CardGroup> call = api.updateCardGroup(defaultCardGroup.getId(), defaultCardGroup);
        call.enqueue(new Callback<CardGroup>() {
            @Override
            public void onResponse(Call<CardGroup> call, Response<CardGroup> response) {
                if(response.isSuccessful()) {
                    defaultCardGroup = response.body();
                    Intent intent = new Intent();
                    AdjustActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    defaultCardGroup.removeCard(card);
                    try {
                        Log.d(TAG, "onResponse: " + new String(response.errorBody().bytes()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CardGroup> call, Throwable t) {
                defaultCardGroup.removeCard(card);
                Log.e(TAG, "onFailure: ", t);
            }
        });


    }

    /**
     * 确认按钮响应
     * @param view
     */
    public void confirmClick(View view) {
        addCard();
    }
}
