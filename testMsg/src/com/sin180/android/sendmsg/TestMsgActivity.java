package com.sin180.android.sendmsg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sin180.android.adapter.MsgAdapter;
import com.sin180.android.view.PullToRefreshListView;
import com.sin180.android.view.PullToRefreshListView.OnRefreshListener;

public class TestMsgActivity extends Activity implements OnClickListener, OnItemClickListener {
	
	private boolean isMore = false;// menu菜单翻页控制
	private AlertDialog menuDialog;// menu菜单Dialog
	private GridView menuGrid;
	private View menuView;
	
	private final int ITEM_SEARCH = 0;// 搜索
	private final int ITEM_FILE_MANAGER = 1;// 文件管理
	private final int ITEM_DOWN_MANAGER = 2;// 下载管理
	private final int ITEM_FULLSCREEN = 3;// 全屏
	private final int ITEM_MORE = 11;// 菜单

	
	/** 菜单图片 **/
	int[] menu_image_array = { R.drawable.menu_search,
			R.drawable.menu_filemanager, R.drawable.menu_downmanager,
			R.drawable.menu_fullscreen, R.drawable.menu_inputurl,
			R.drawable.menu_bookmark, R.drawable.menu_bookmark_sync_import,
			R.drawable.menu_sharepage, R.drawable.menu_quit,
			R.drawable.menu_nightmode, R.drawable.menu_refresh,
			R.drawable.menu_more };
	/** 菜单文字 **/
	String[] menu_name_array = { "搜索", "文件管理", "下载管理", "全屏", "网址", "书签",
			"加入书签", "分享页面", "退出", "夜间模式", "刷新", "更多" };
	/** 菜单图片2 **/
	int[] menu_image_array2 = { R.drawable.menu_auto_landscape,
			R.drawable.menu_penselectmodel, R.drawable.menu_page_attr,
			R.drawable.menu_novel_mode, R.drawable.menu_page_updown,
			R.drawable.menu_checkupdate, R.drawable.menu_checknet,
			R.drawable.menu_refreshtimer, R.drawable.menu_syssettings,
			R.drawable.menu_help, R.drawable.menu_about, R.drawable.menu_return };
	/** 菜单文字2 **/
	String[] menu_name_array2 = { "自动横屏", "笔选模式", "阅读模式", "浏览模式", "快捷翻页",
			"检查更新", "检查网络", "定时刷新", "设置", "帮助", "关于", "返回" };
	
	private static String MYLOGFILEName = "_Log.log";
	private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");
	private List<MsgVo> data;
	private PullToRefreshListView prlistView;
	private MsgAdapter videoBaseAdpter;
	private String needWriteFiel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
		initMenuView();
	}
	private void initMenuView() {
		menuView = View.inflate(this, R.layout.gridview_menu, null);
		// 创建AlertDialog
		menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		menuDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
					dialog.dismiss();
				return false;
			}
		});
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		/** 监听menu选项 **/
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2) {
				case ITEM_SEARCH:// 搜索

					break;
				case ITEM_FILE_MANAGER:// 文件管理

					break;
				case ITEM_DOWN_MANAGER:// 下载管理

					break;
				case ITEM_FULLSCREEN:// 全屏

					break;
				case ITEM_MORE:// 翻页
					if (isMore) {
						menuGrid.setAdapter(getMenuAdapter(menu_name_array2,menu_image_array2));
						isMore = false;
					} else {// 首页
						menuGrid.setAdapter(getMenuAdapter(menu_name_array,menu_image_array));
						isMore = true;
					}
					menuGrid.invalidate();// 更新menu
					menuGrid.setSelection(ITEM_MORE);
					break;
				}
				
				
			}
		});		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu"); 
		return super.onCreateOptionsMenu(menu);
	}
	
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
		} else {
			menuDialog.show();
		}
		return false;// 返回为true 则显示系统menu
	}
	

	private void init() {
		prlistView = (PullToRefreshListView) findViewById(R.id.video_list);
		prlistView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});
		needWriteFiel = logfile.format(new Date());
		data = new ArrayList<MsgVo>();
		new GetDataTask().execute();

	}

	/**
	 * 刷新数据
	 * 
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<MsgVo>> {
		@Override
		protected List<MsgVo> doInBackground(Void... params) {
			try {
				data = getStringList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(List<MsgVo> data) {
			videoBaseAdpter = new MsgAdapter(TestMsgActivity.this,
					R.layout.video_item, data);
			prlistView.setAdapter(videoBaseAdpter);
			prlistView.onRefreshComplete();
			videoBaseAdpter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}

	private ArrayList<MsgVo> getStringList() {
		ArrayList<MsgVo> mList = new ArrayList<MsgVo>();
		String str = FileUtil.readFile(needWriteFiel + MYLOGFILEName);
		String strValue = StringUtil.decode(str);
		String[] msgList = strValue.split(":.:");
		for (int i = 0; i < msgList.length; i++) {
			System.out.println(msgList.length + "---" + msgList[i]);
			String msg = msgList[i];
			String[] msgInfo = msg.split("&@");
			for (int j = 0; j < msgInfo.length; j++) {
				System.out.println(msgInfo[j]);
			}
			if (msgInfo.length == 4) {
				MsgVo mv = new MsgVo();
				mv.setMsgTime(msgInfo[0]);
				mv.setMsgSend(msgInfo[1]);
				mv.setMsgType(msgInfo[2]);
				mv.setMsgData(msgInfo[3]);
				mList.add(mv);
			}
		}
		return mList;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	@Override
	public void onClick(View v) {
	}

	public void search(View v) {
		try {
			EditText timeEdit = (EditText) findViewById(R.id.search_time);
			String time = timeEdit.getText().toString().trim();
			if (TextUtils.isEmpty(time)) {
				Toast.makeText(this, "search is null", Toast.LENGTH_SHORT).show();
				return;
			}
			Date date = logfile.parse(time);
			needWriteFiel = logfile.format(date);
			new GetDataTask().execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}