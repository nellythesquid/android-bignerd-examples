package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {
	
	private static final String TAG = "QuizActiviy";
	private static final String KEY_INDEX = "index";
	private static final String KEY_CHEAT = "cheat";
	private static final String KEY_CHEAT_ARRAY = "cheat_array";
	
	private Button mTrueButton;
	private Button mFalseButton;
	private Button mCheatButton; 
	private ImageButton mNextButton;
	private ImageButton mPrevButton;	
	private TextView mQuestionTextView;
	
	private boolean[] mAreCheaters;
	
	private TrueFalse[] mQuestionBank = new TrueFalse[] {
			new TrueFalse(R.string.question_oceans, true),
			new TrueFalse(R.string.question_mideast, false),
			new TrueFalse(R.string.question_africa, false),
			new TrueFalse(R.string.question_americas, true),
			new TrueFalse(R.string.question_asia, true),
	};
	
	private int mCurrentIndex = 0;
	boolean mCheater = false;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null) {
			return;
		}
		mCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
		didCheat(mCheater);
	}
	
	private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
		//mCheater = false;
	}
	
	private boolean isCheater() {
		return mAreCheaters[mCurrentIndex];
	}
	
	private void didCheat(boolean cheated) {
		mAreCheaters[mCurrentIndex] = cheated;
	}
	
	private void checkAnswer(boolean userPressedTrue) {
		boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
		int messageResId = 0;
		
		if( isCheater() ) {
			messageResId = R.string.judgment_toast;
		} else {
			if(userPressedTrue == answerIsTrue) {
				messageResId = R.string.correct_toast;
			}else {
				messageResId = R.string.incorrect_toast;
			}	
		}

		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
        
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        
        
        if (savedInstanceState != null) {
        	mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        
        updateQuestion();
        
        mAreCheaters = new boolean[mQuestionBank.length];
        
        if (savedInstanceState != null) {
        	//mCheater = savedInstanceState.getBoolean(KEY_CHEAT, false);
        	mAreCheaters = savedInstanceState.getBooleanArray(KEY_CHEAT_ARRAY);
        }else {
        	for(int i=0;i<mQuestionBank.length;i++) {
        		mAreCheaters[i] = false;
        	}
        }
        
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(QuizActivity.this, CheatActivity.class);	//Start CheatActivity
				boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
				i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
				startActivityForResult(i,0);
			}
		});       
        
        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkAnswer(true);		
			}
		});
        
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkAnswer(false);	
			}
		});
        
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				updateQuestion();
			}
		});  
        
        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + (mQuestionBank.length - 1)) % mQuestionBank.length;
				updateQuestion();
			}
		});  
        
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				updateQuestion();
			}
		});  
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
    	Log.i(TAG, "onSaveInstanceState");
    	savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    	//savedInstanceState.putBoolean(KEY_CHEAT, mCheater);
    	savedInstanceState.putBooleanArray(KEY_CHEAT_ARRAY, mAreCheaters);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	Log.d(TAG, "onStart() called");
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	Log.d(TAG, "onPause() called");
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.d(TAG, "onResume() called");
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	Log.d(TAG, "onStop() called");
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Log.d(TAG, "onDestroy() called");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }
    
}
