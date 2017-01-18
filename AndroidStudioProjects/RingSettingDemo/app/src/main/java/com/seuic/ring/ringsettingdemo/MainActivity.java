package com.seuic.ring.ringsettingdemo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {
	private FragmentManager fragmentManager;
	private final Fragment[] fragments = new Fragment[] { new FragmentScanner(), new FragmentSetting(),
			new FragmentOtherSetting() };
	private Fragment fragment = null;
	private TextView mTxtScanner;
	private TextView mTxtSetting;
	private TextView mTxtVersion;
	private TextView mTxtOtherSetting;

	private int mChoice = -1;
	private int mBeferChoice = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((FragmentSetting) fragments[1]).setmContext(this);
		((FragmentOtherSetting) fragments[2]).setmContext(this);
		initView();

		mTxtScanner.setTextColor(Color.RED);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.add(R.id.frl_content, fragments[0]);
		transaction.commit();

		mBeferChoice = 0;
		mChoice = 0;

	}

	private void initView() {

		fragmentManager = getFragmentManager();

		mTxtVersion = (TextView) findViewById(R.id.txt_version);
		mTxtVersion.setText("ver:" + getVersionName());
		mTxtScanner = (TextView) findViewById(R.id.scanner);
		mTxtSetting = (TextView) findViewById(R.id.setting);
		mTxtOtherSetting = (TextView) findViewById(R.id.othersetting);

		mTxtScanner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBeferChoice != 0) {
					mChoice = 0;
					changeFragment();
				}
			}
		});

		mTxtSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((FragmentSetting) fragments[1]).refresh();
				if (mBeferChoice != 1) {
					mChoice = 1;
					changeFragment();
				}
			}
		});
		mTxtOtherSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((FragmentOtherSetting) fragments[2]).refreshConnect();
				if (mBeferChoice != 2) {
					mChoice = 2;
					changeFragment();
				}
			}
		});

	}

	private void changeFragment() {

		if (mChoice == 0) {
			switchContent(fragments[mBeferChoice], fragments[0]);
			mTxtScanner.setTextColor(Color.RED);
			mTxtSetting.setTextColor(Color.BLACK);
			mTxtOtherSetting.setTextColor(Color.BLACK);
			mBeferChoice = 0;

		} else if (mChoice == 1) {
			switchContent(fragments[mBeferChoice], fragments[1]);
			mTxtSetting.setTextColor(Color.RED);
			mTxtScanner.setTextColor(Color.BLACK);
			mTxtOtherSetting.setTextColor(Color.BLACK);
			mBeferChoice = 1;

		} else if (mChoice == 2) {

			switchContent(fragments[mBeferChoice], fragments[2]);
			mTxtOtherSetting.setTextColor(Color.RED);
			mTxtScanner.setTextColor(Color.BLACK);
			mTxtSetting.setTextColor(Color.BLACK);
			mBeferChoice = 2;
		}

	}

	public Fragment getFragment(int i) {
		return fragments[i];
	}

	public void switchContent(Fragment from, Fragment to) {
		if (fragment != to) {
			fragment = to;
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(from).add(R.id.frl_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	public String getVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
