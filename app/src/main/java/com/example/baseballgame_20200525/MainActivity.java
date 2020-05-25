package com.example.baseballgame_20200525;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.baseballgame_20200525.adapters.MessageAdapter;
import com.example.baseballgame_20200525.databinding.ActivityMainBinding;
import com.example.baseballgame_20200525.datas.Message;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

//    문제로 사용될 3자리 숫자 배열
    int[] questionArr = new int[3];

//    정답입력횟수
    int tryCount = 0;

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

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                전송 버튼 누르면 => 타이핑 된 값을 받아오기.
                String inputValue =binding.numEdt.getText().toString();

//                3자리가 아니면 등록 거부
                if(inputValue.length()!=3){
                    Toast.makeText(mContext, "3자리 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                새로운 메세지로 등록.
                messages.add(new Message(inputValue, "Me"));

//                리스트뷰의 내용물에 변화 발생
                messageAdapter.notifyDataSetChanged();

//                리스트뷰를 맨 밑으로 끌어내려주자.
                binding.messageListView.smoothScrollToPosition(messages.size()-1);

//                올바른 입력이 들어오면 시도 횟수 증가 처리.
                tryCount++;
//                ?S??B인지 계산하고 답장.
                checkStrikeAndBalls(inputValue);
            }
        });

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
    void checkStrikeAndBalls(String inputVal){
//        String=> int로 변경 => int[] 3자리로 변경.
        int inputNum = Integer.parseInt(inputVal);

        int[] myNumbers = new int[3];
        myNumbers[0] = inputNum / 100;
        myNumbers[1] = inputNum / 10 % 10;
        myNumbers[2] = inputNum % 10;


        int strikeCount=0;
        int ballCount=0;
//        myNumbers 와  questionArr간의 비교

        for (int i=0; i<myNumbers.length; i++){

            for(int j =0; j<questionArr.length; j++){

//                같은 숫자 발견,
                if(myNumbers[i] == questionArr[j]){
//                    index가 같으니 strike
                    if(i==j){
                        strikeCount++;

                    }
                    else{
//                        숫자는 같지만 index가 다르니까 ball발견
                        ballCount++;
                    }
                }
            }

        }


        final int copyStrike = strikeCount;
        final int copyBall = ballCount;
        Handler myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

//        컴퓨터가 ?s?b인지 답장하고, 밑으로 끌어내리기.
                messages.add(new Message(String.format("%dS %dB 입니다.",copyStrike,copyBall),"Cpu"));
                messageAdapter.notifyDataSetChanged();
                binding.messageListView.smoothScrollToPosition(messages.size()-1);
            }
        },500);



//        3s라면 축하메세지 + 몇번만에 맞췄는지 + 입력불가하도록 막아주기.
        if(strikeCount==3){
            messages.add(new Message("정답입니다","Cpu"));
            messages.add(new Message(String.format("%d회만에 맞췄습니다.",tryCount),"Cpu"));

            messageAdapter.notifyDataSetChanged();
            binding.messageListView.smoothScrollToPosition(messages.size()-1);


//            EditText와 버튼을 더이상 사용하지 못하도록 막아주는 enabled false코드.
            binding.numEdt.setEnabled(false);
            binding.sendBtn.setEnabled(false);

//            종료 안내 토스트.
            Toast.makeText(mContext, "이용해 주셔서 감사합니다.", Toast.LENGTH_SHORT).show();

        }
    }
}
