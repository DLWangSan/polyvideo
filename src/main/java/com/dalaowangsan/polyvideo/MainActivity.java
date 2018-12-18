package com.dalaowangsan.polyvideo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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
	private static final int MONEY = 6;
	private static final int LEFT = 7;

	public static String interface_1;
    public static String interface_2;
    public static String news;
    public static String version;
    public static String money_num;
    public static String money_url;

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
		ClipData mClipData = ClipData.newPlainText("Label", money_num);
		// 将ClipData内容放到系统剪贴板里。
		cm.setPrimaryClip(mClipData);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity OnCreate
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_advanced);

		firstRun();

		try {
			init();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();

//		//状态栏透明
//		UltimateBar ultimateBar = new UltimateBar(this);
//		ultimateBar.setImmersionBar();

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
							AlertDialog.Builder dialog1 = new AlertDialog.Builder(MainActivity.this);
							dialog1.setTitle("聚合视频");
							dialog1.setPositiveButton("现在领红包", new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									copyMoney();
									Intent intent = new Intent();
//									intent.setAction("android.intent.action.VIEW");
									intent.setData(Uri.parse("alipayqr://platformapi/startapp"));
									startActivity(intent);
									Toast.makeText(MainActivity.this, "点击最上方搜索框，长按，选择“粘贴", Toast.LENGTH_LONG).show();
								}
							});

							dialog1.setMessage("点击“现在领红包”后，会跳转到支付宝。" +
									"我们已经为您复制好红包码，直接在搜索框粘贴，就能领取支付宝红包。" +
									"谢谢您的支持！");

							dialog1.create().show();
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
				R.drawable.youku, R.drawable.manggo, R.drawable.sohu, R.drawable.pptv,R.drawable.money,R.drawable.left};

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



						case MONEY:

							AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
							dialog.setTitle("聚合视频");

							dialog.setPositiveButton("现在领红包", new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									copyMoney();
									Intent intent = new Intent();
//									intent.setAction("android.intent.action.VIEW");
									intent.setData(Uri.parse("alipayqr://platformapi/startapp"));
									startActivity(intent);
									Toast.makeText(MainActivity.this, "点击最上方搜索框，长按，选择“粘贴", Toast.LENGTH_LONG).show();
								}
							});

							dialog.setMessage("点击“现在领红包”后，会跳转到支付宝。" +
									"我们已经为您复制好红包码，直接在搜索框粘贴，就能领取支付宝红包。" +
									"谢谢您的支持！");

							dialog.create().show();
							break;

						case LEFT:

							AlertDialog.Builder dialog1 = new AlertDialog.Builder(MainActivity.this);
							dialog1.setTitle("聚合视频");

							dialog1.setPositiveButton("现在领红包", new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									copyMoney();
									Intent intent = new Intent();
//									intent.setAction("android.intent.action.VIEW");
									intent.setData(Uri.parse("alipayqr://platformapi/startapp"));
									startActivity(intent);
									Toast.makeText(MainActivity.this, "点击最上方搜索框，长按，选择“粘贴", Toast.LENGTH_LONG).show();
								}
							});

							dialog1.setMessage("点击“现在领红包”后，会跳转到支付宝。" +
									"我们已经为您复制好红包码，直接在搜索框粘贴，就能领取支付宝红包。" +
									"谢谢您的支持！");

							dialog1.create().show();
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

		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle("聚合视频");
		dialog.setPositiveButton("我知道了，退出", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Process.killProcess(Process.myPid());
			}
		});

		dialog.setMessage("如果在使用过程中发现问题，请在葫芦侠、吾爱论坛、或QQ群反馈。");
		dialog.create().show();
	}

	private void firstRun() {
		SharedPreferences sharedPreferences=this.getSharedPreferences("share",MODE_PRIVATE);
		boolean isFirstRun=sharedPreferences.getBoolean("isFirstRun", true);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		if(isFirstRun){

			AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
			dialog.setTitle("聚合视频");
			dialog.setNegativeButton("我知道了", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
			dialog.setMessage("检测到您是第一次使用。软件嵌入X5内核，" +
					"第一次进入视频网站可能会出现卡顿现象。进入视频网站点击左上角播放即可享受Vip观影体验。");
			dialog.create().show();
			editor.putBoolean("isFirstRun", false);
			editor.commit();
		}else{

		}

	}
	protected void init() throws  InterruptedException{
		Thread thread=new Thread(){
			@Override
			public void run() {
				try {
					OkHttpClient client = new OkHttpClient();
					Request request = new Request.Builder()
							.url("http://dlws.show.0552web.com/polyvideo/interface.json")
							.build();
					Response response = client.newCall(request).execute();
					String responseData = response.body().string();
					parseJSON(responseData);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		};

		thread.start();
		thread.join(); //等待thread线程完成后再进行其他操作

		if(version.equals("2.1"))
		{
			Toast.makeText(this, "已是最新版本", Toast.LENGTH_SHORT).show();
		}else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
			dialog.setTitle("聚合视频");
			dialog.setPositiveButton("网盘更新", new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String urlQQ = "https://www.lanzous.com/b526376/";
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlQQ)));

					Process.killProcess(Process.myPid());
				}
			});
			dialog.setNegativeButton("退出",new AlertDialog.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Process.killProcess(Process.myPid());
				}
			});
			dialog.setMessage("旧版本已经失效，请下载最新版本使用！");
			dialog.setCancelable(false);
			dialog.create().show();
		}

		if (news.equals("这是一条公告")){
		}else{
			AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
			dialog.setTitle("公告");
			dialog.setMessage(news);
			dialog.setNegativeButton("我知道了",new AlertDialog.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.create().show();
		}
	}

	protected void parseJSON(String responseData){

        Gson gson = new Gson();
        List<Poly> polyList = gson.fromJson(responseData, new TypeToken<List<Poly>>() {
        }.getType());

        for(Poly poly : polyList){
            if(poly.getId().equals("i_1")){
                interface_1 = poly.getRsc();
                continue;
            }
            if(poly.getId().equals("i_2")){
                interface_2 = poly.getRsc();
                continue;
            }
            if(poly.getId().equals("news")){
                news = poly.getRsc();
                continue;
            }

			if(poly.getId().equals("version")){
				version = poly.getRsc();
				continue;
			}
			if(poly.getId().equals("money_num")){
				money_num = poly.getRsc();
				continue;
			}
			if(poly.getId().equals("money_url")){
				money_url = poly.getRsc();
			}
        }

	}
}
