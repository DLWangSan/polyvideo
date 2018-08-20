package com.dalaowangsan.polyvideo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static String[] titles = null;

	private DrawerLayout drawerLayout;
	private NavigationView navigationView;
	private Button navButton;
	private String playable;


	// /////////////////////////////////////////////////////////////////////////////////////////////////
	// add constant here
	private static final int AQIYI = 0;
	private static final int TECENT = 1;
	private static final int YOUKU = 2;
	private static final int MANGGO = 3;
	private static final int SOUHU = 4;
	private static final int PPTV = 5;

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// for view init
	private Context mContext = null;
	private SimpleAdapter gridAdapter;
	private GridView gridView;
	private ArrayList<HashMap<String, Object>> items;

	private static boolean main_initialized = false;

	public void copyMoney() {
		//获取剪贴板管理器：
		ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		// 创建普通字符型ClipData
		ClipData mClipData = ClipData.newPlainText("Label", "J7B9Tq8379");
		// 将ClipData内容放到系统剪贴板里。
		cm.setPrimaryClip(mClipData);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity OnCreate
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		copyMoney();
		setContentView(R.layout.activity_main_advanced);
		copyMoney();
		firstRun();

		drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		navigationView.setItemIconTintList(null);
		navButton = (Button) findViewById(R.id.nav_button);


		navButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.openDrawer(navigationView);
			}

		});


		//获取头布局文件
		View headerView = navigationView.getHeaderView(0);

		//头部点击事件
		headerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playable = "F";
				String data = "http://github.com/DLWangsan/";
				Intent intent = new Intent(getBaseContext(), BrowserActivity.class);
				intent.putExtra("extra_data", data);
				intent.putExtra("playable", playable);
				startActivityForResult(intent, 1);
//				Toast.makeText(getBaseContext(),"点击了头部",Toast.LENGTH_SHORT).show();

			}
		});


		//Item点击事件
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem item) {
				int id = item.getItemId();
				switch (id) {
					case R.id.alibaba:
						try {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							//实现payUrl
							String payUrl = "HTTPS://QR.ALIPAY.COM/FKX02636LQBSU6EAWSPS14";
							intent.setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + payUrl));
							startActivity(intent);
						} catch (Exception e) {
							Toast.makeText(getBaseContext(), "打开失败，请检查是否安装了支付宝！", Toast.LENGTH_SHORT).show();
						}
						break;
					case R.id.wechat:
						try {
							//利用Intent打开微信
							Uri uri = Uri.parse("weixin://");
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							Toast.makeText(getBaseContext(), "能力有限，没写出来，只能跳转到这里。欢迎大神指点", Toast.LENGTH_LONG).show();
							startActivity(intent);
						} catch (Exception e) {
							Toast.makeText(getBaseContext(), "无法跳转到微信，请检查您是否安装了微信！", Toast.LENGTH_SHORT).show();
						}
						break;
					case R.id.qq:
						android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
								.setIcon(R.mipmap.yyvideologo)//设置标题的图片
								.setTitle("QQ捐赠")//设置对话框的标题
								.setMessage("加群即可捐赠支持我们")//设置对话框的内容
								//设置对话框的按钮
								.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								})
								.setPositiveButton("现在加群", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										Toast.makeText(MainActivity.this, "请选择QQ，加入官方QQ群", Toast.LENGTH_LONG).show();
										String urlQQ = "http://qm.qq.com/cgi-bin/qm/qr?k=YT8TuNFOvmB-rOEa7_vB2a-tK1kwpbeW";
										startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlQQ)));
									}
								}).create();

						dialog.show();
//						try {
//
//
//							String urlQQ ="https://i.qianbao.qq.com/wallet/sqrcode.htm?m=tenpay&f=wallet&u=1329864438&a=1&n=%E2%9C%8E%EF%B9%8F%E4%B8%8D%E5%8F%AF%E9%A2%84%E" +
//									"8%A7%81&ac=6CCECD41655AD88DBC75962135704FB8BD2828AE0C889B952F014B811F1BA432";
//							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlQQ)));
//
//							Toast.makeText(getBaseContext(),"能力有限，没写出来，只能跳转到这里。欢迎大神指点",Toast.LENGTH_LONG).show();
//						} catch (Exception e) {
//							Toast.makeText(getBaseContext(), "无法跳转到微信，请检查您是否安装了微信！", Toast.LENGTH_SHORT).show();
//						}
						break;
					case R.id.join:
						Toast.makeText(getBaseContext(), "请选择QQ，加入官方QQ群", Toast.LENGTH_LONG).show();
						String urlQQ = "http://qm.qq.com/cgi-bin/qm/qr?k=YT8TuNFOvmB-rOEa7_vB2a-tK1kwpbeW";
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlQQ)));
						break;
					case R.id.developer:
						String data = "http://github.com/DLWangsan/";
						playable = "F";
						Intent intent = new Intent(getBaseContext(), BrowserActivity.class);
						intent.putExtra("extra_data", data);
						intent.putExtra("playable", playable);
						startActivityForResult(intent, 10);
						break;
				}

				//在这里处理item的点击事件
				return true;
			}
		});


		mContext = this;
		if (!main_initialized) {
			this.new_init();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity OnResume
	@Override
	protected void onResume() {
		this.new_init();

		// this.gridView.setAdapter(gridAdapter);
		super.onResume();
	}

	// ////////////////////////////////////////////////////////////////////////////////
	// initiate new UI content
	private void new_init() {
		items = new ArrayList<HashMap<String, Object>>();
		this.gridView = (GridView) this.findViewById(R.id.item_grid);

		if (gridView == null)
			throw new IllegalArgumentException("the gridView is null");

		titles = getResources().getStringArray(R.array.index_titles);
		int[] iconResourse = {R.drawable.aqiyi, R.drawable.tecent,
				R.drawable.youku, R.drawable.manggo, R.drawable.sohu, R.drawable.pptv};

		HashMap<String, Object> item = null;
		// HashMap<String, ImageView> block = null;
		for (int i = 0; i < titles.length; i++) {
			item = new HashMap<String, Object>();
			item.put("title", titles[i]);
			item.put("icon", iconResourse[i]);

			items.add(item);
		}
		this.gridAdapter = new SimpleAdapter(this, items,
				R.layout.function_block, new String[]{"title", "icon"},
				new int[]{R.id.Item_text, R.id.Item_bt});
		if (null != this.gridView) {
			this.gridView.setAdapter(gridAdapter);
			this.gridAdapter.notifyDataSetChanged();
			this.gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> gridView, View view,
										int position, long id) {
					Intent intent = null;
					switch (position) {
						case AQIYI:
							playable = "T";
							String data0 = "http://www.iqiyi.com/";
							intent = new Intent(MainActivity.this, BrowserActivity.class);
							intent.putExtra("extra_data", data0);
							intent.putExtra("playable", playable);
							startActivityForResult(intent, 0);
							break;
						case TECENT:
							playable = "T";
							String data1 = "http://m.v.qq.com/";
							intent = new Intent(MainActivity.this, BrowserActivity.class);
							intent.putExtra("extra_data", data1);
							intent.putExtra("playable", playable);
							startActivityForResult(intent, 1);
							break;

						case YOUKU:
							playable = "T";
							String data2 = "https://www.youku.com/";
							intent = new Intent(MainActivity.this, BrowserActivity.class);
							intent.putExtra("extra_data", data2);
							intent.putExtra("playable", playable);
							startActivityForResult(intent, 2);
							break;
						case MANGGO:
							playable = "T";
							String data3 = "https://m.mgtv.com/channel/home/";
							intent = new Intent(MainActivity.this, BrowserActivity.class);
							intent.putExtra("extra_data", data3);
							intent.putExtra("playable", playable);
							startActivityForResult(intent, 3);
							break;
						case SOUHU:
							playable = "T";
							String data4 = "https://m.tv.sohu.com/";
							intent = new Intent(MainActivity.this, BrowserActivity.class);
							intent.putExtra("extra_data", data4);
							intent.putExtra("playable", playable);
							startActivityForResult(intent, 4);
							break;
						case PPTV:
							playable = "T";
							String data5 = "http://m.pptv.com/";
							intent = new Intent(MainActivity.this, BrowserActivity.class);
							intent.putExtra("extra_data", data5);
							intent.putExtra("playable", playable);
							startActivityForResult(intent, 5);
							break;

					}

				}
			});

		}
		main_initialized = true;

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// Activity menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				this.tbsSuiteExit();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void tbsSuiteExit() {
		copyMoney();
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle("聚合视频");
		dialog.setPositiveButton("确定退出", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				copyMoney();
				Process.killProcess(Process.myPid());
			}
		});
		dialog.setNegativeButton("加群反馈", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						copyMoney();
						Toast.makeText(MainActivity.this, "请选择QQ，加入官方QQ群", Toast.LENGTH_LONG).show();
						String urlQQ = "http://qm.qq.com/cgi-bin/qm/qr?k=YT8TuNFOvmB-rOEa7_vB2a-tK1kwpbeW";
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlQQ)));
					}

				}
		);
		dialog.setMessage("确定要退出吗？如果在使用过程中发现问题，请加群反馈");
		dialog.create().show();
	}

	private void firstRun() {
		SharedPreferences sharedPreferences=this.getSharedPreferences("share",MODE_PRIVATE);
		boolean isFirstRun=sharedPreferences.getBoolean("isFirstRun", true);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		if(isFirstRun){
			copyMoney();
			AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
			dialog.setTitle("聚合视频");
			dialog.setNegativeButton("我知道了", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					copyMoney();
				}
			});
			dialog.setMessage("检测到您是第一次使用。软件嵌入X5内核，" +
					"第一次进入视频网站可能会出现卡顿现象。进入视频网站点击左上角播放即可享受Vip观影体验。");
			dialog.create().show();
			editor.putBoolean("isFirstRun", false);
			editor.commit();
		}else{
			copyMoney();
		}

	}
}
