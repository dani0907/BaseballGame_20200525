package com.example.baseballgame_20200525;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.example.baseballgame_20200525.adapters.MessageAdapter;
import com.example.baseballgame_20200525.databinding.ActivityMainBinding;
import com.example.baseballgame_20200525.datas.Message;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

//    문제로 사용될 3자리 숫자 배열
    int[] questionArr = new int[3];

//    채팅 내역으로 사용할 ArrayList
    List<Message> messages = new ArrayList<>();

//    Adapter를 변수로 만들고 실제 활용 => onCreate이후에 객체화.
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

    }

    @Override
    public void setValues() {

        messageAdapter = new MessageAdapter(mContext,R.layout.message_list_item,messages);
        binding.messageListView.setAdapter(messageAdapter);

        makeQuestion();

    }

//    문제로 나올 3자리 숫자를 입력
    void makeQuestion(){
//        세 자리 숫자를 채우기 위한 for
        for (int i=0; i<questionArr.length ; i++){
//            각 자리에 올바른 숫자가 들어갈 때까지 무한 반복.
            while(true){
//                1에서 9까지의 정수
                int randomNum = (int)(Math.random() * 9 + 1);

                boolean isDuplOk = true;
//                중복 검사 로직 (같은 숫자가 하나라도 있는지 조회)
                for (int num : questionArr){
//                    문제에서 같은 숫자를 찾았다 => 중복검사 통과 X
                    if(num == randomNum){
                        isDuplOk = false;
                        break;
                    }
                }
//                중복검사 통과?
                if(isDuplOk){
//                    배열에 문제로 이 숫자를 채택.
                    questionArr[i] = randomNum;
//                    올바른 숫자를 뽑았으니 무한 반복 종료 => 다음 숫자 뽑으러 이동.
                    break;
                }

            }
        }
        for(int num : questionArr){
            Log.d("문제 숫자", num +"");
        }

//        컴퓨터가 사람에게 환영 메세지.

        messages.add(new Message("숫자 야구게임에 오신 것을 환영합니다.","Cpu"));
        messages.add(new Message("세 자리 숫자를 맞춰주세요.","Cpu"));
        messages.add(new Message("1~9만 출제되며, 중복된 숫자는 없습니다.","Cpu"));

//        어댑터가 사용하는 list의 내용 변경 (메세지추가)이 생겼으니 새로고침.
        messageAdapter.notifyDataSetChanged();
    }
}
