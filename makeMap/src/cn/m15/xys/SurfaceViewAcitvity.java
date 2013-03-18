package cn.m15.xys;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class SurfaceViewAcitvity extends Activity implements OnTouchListener,
		OnGestureListener {

	AnimView mAnimView = null;
	private GestureDetector detector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏显示窗口
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 获取屏幕宽高
		Display display = getWindowManager().getDefaultDisplay();

		// 显示自定义的游戏View
		mAnimView = new AnimView(this, display.getWidth(), display.getHeight());
		setContentView(mAnimView);
		mAnimView.setOnTouchListener(this);
		detector = new GestureDetector(this);
	}

	public class AnimView extends SurfaceView implements Callback, Runnable {
		/** 向下移动动画 **/
		public final static int ANIM_DOWN = 0;
		/** 向左移动动画 **/
		public final static int ANIM_LEFT = 1;
		/** 向右移动动画 **/
		public final static int ANIM_RIGHT = 2;
		/** 向上移动动画 **/
		public final static int ANIM_UP = 3;
		/** 动画的总数量 **/
		public final static int ANIM_COUNT = 4;

		Animation mHeroAnim[] = new Animation[ANIM_COUNT];

		Paint mPaint = null;

		/** 任意键被按下 **/
		private boolean mAllkeyDown = true;
		/** 按键下 **/
		private boolean mIskeyDown = false;
		/** 按键左 **/
		private boolean mIskeyLeft = false;
		/** 按键右 **/
		private boolean mIskeyRight = false;
		/** 按键上 **/
		private boolean mIskeyUp = false;

		// 当前绘制动画状态ID
		int mAnimationState = 0;

		// tile块的宽高
		public final static int TILE_WIDTH = 32;
		public final static int TILE_HEIGHT = 32;

		// 缓冲块的宽高的数量
		public final static int BUFFER_WIDTH_COUNT = 10;
		public final static int BUFFER_HEIGHT_COUNT = 15;

		// 场景的宽高
		public final static int SCENCE_WIDTH = 480;
		public final static int SCENCE_HEIGHT = 800;

		// 场景偏移量 未到场景边界地图向回滚动
		public final static int SCENCE_OFFSET = 3;
		public final static int SCENCE_OFFSET_WIDTH = 100;

		// 场景块的宽高的数量
		public final static int TILE_WIDTH_COUNT = 15;
		public final static int TILE_HEIGHT_COUNT = 25;

		// 数组元素为0则什么都不画
		public final static int TILE_NULL = 0;
		// 第一层游戏View地图数组
		public int[][] mMapView = {
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

		// 第二层游戏实体actor数组
		public int[][] mMapAcotor = {
				{ 5, 6, 7, 0, 3, 4, 3, 4, 3, 4, 0, 0, 2, 2, 0 },
				{ 13, 14, 15, 0, 11, 12, 11, 12, 11, 12, 0, 0, 10, 10, 0 },
				{ 21, 22, 23, 0, 19, 20, 19, 20, 19, 20, 0, 0, 18, 18, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
				{ 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10 },
				{ 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18 },
				{ 8, 8, 8, 8, 9, 0, 0, 0, 0, 0, 9, 8, 8, 8, 8 },
				{ 8, 8, 8, 8, 17, 0, 0, 0, 0, 0, 17, 8, 8, 8, 8 },
				{ 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 5, 6, 7 },
				{ 11, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 13, 14, 15 },
				{ 19, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 21, 22, 23 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 8, 8, 8, 8, 9, 0, 0, 0, 0, 0, 9, 8, 8, 8, 8 },
				{ 8, 8, 8, 8, 17, 0, 0, 0, 0, 0, 17, 8, 8, 8, 8 },
				{ 5, 6, 7, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4 },
				{ 13, 14, 15, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 12 },
				{ 21, 22, 23, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 20 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

		// 第三层游戏碰撞物理层数组
		public int[][] mCollision = {
				{ -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, 0 },
				{ -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, 0 },
				{ -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
				{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
				{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
				{ -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

		// 游戏地图资源
		Bitmap mBitmap = null;

		// 资源文件
		Resources mResources = null;

		// 横向纵向tile块的数量
		int mWidthTileCount = 0;
		int mHeightTileCount = 0;

		// 横向纵向tile块的数量
		int mBitMapWidth = 0;
		int mBitMapHeight = 0;

		// 英雄在地图中的坐标以英雄脚底中心为原点
		int mHeroPosX = 0;
		int mHeroPosY = 0;

		// 备份英雄发生碰撞以前的坐标点
		int mBackHeroPosX = 0;
		int mBackHeroPosY = 0;

		// 备份英雄发生碰撞以前的屏幕显示坐标点
		int mBackHeroScreenX = 0;
		int mBackHeroScreenY = 0;

		// 英雄在地图中绘制坐标
		int mHeroImageX = 0;
		int mHeroImageY = 0;

		// 英雄在行走范围中绘制坐标
		int mHeroScreenX = 0;
		int mHeroScreenY = 0;

		// 英雄在地图二位数组中的索引
		int mHeroIndexX = 0;
		int mHeroIndexY = 0;

		// 屏幕宽高才尺寸
		int mScreenWidth = 0;
		int mScreenHeight = 0;

		// 缓冲区域数据的index
		int mBufferIndexX = 0;
		int mBufferIndexY = 0;

		// 地图的坐标
		int mMapPosX = 0;
		int mMapPosY = 0;

		/** 人物图片资源与实际英雄脚底板坐标的偏移 **/
		public final static int OFF_HERO_X = 16;
		public final static int OFF_HERO_Y = 35;

		/** 主角行走步长 **/
		public  int HERO_STEP = 60;

		/** 与实体层发生碰撞 **/
		private boolean isAcotrCollision = false;
		/** 与边界层发生碰撞 **/
		private boolean isBorderCollision = false;
		/** 游戏主线程 **/
		private Thread mThread = null;
		/** 线程循环标志 **/
		private boolean mIsRunning = false;

		private SurfaceHolder mSurfaceHolder = null;
		private Canvas mCanvas = null;

		/**
		 * 构造方法
		 * 
		 * @param context
		 */
		public AnimView(Context context, int screenWidth, int screenHeight) {
			super(context);
			mPaint = new Paint();
			mScreenWidth = screenWidth;
			mScreenHeight = screenHeight;
			initAnimation(context);
			initMap(context);
			initHero();

			/** 获取mSurfaceHolder **/
			mSurfaceHolder = getHolder();
			mSurfaceHolder.addCallback(this);
			setFocusable(true);
		}

		private void initHero() {
			mHeroImageX = 100;
			mHeroImageY = 100;
			/** 根据图片显示的坐标算出英雄脚底的坐标 **/
			/** X轴+图片宽度的一半 Y轴加图片的高度 **/
			mHeroPosX = mHeroImageX + OFF_HERO_X;
			mHeroPosY = mHeroImageY + OFF_HERO_Y;
			mHeroIndexX = mHeroPosX / TILE_WIDTH;
			mHeroIndexY = mHeroPosY / TILE_HEIGHT;
			mHeroScreenX = mHeroPosX;
			mHeroScreenY = mHeroPosY;
		}

		private void initMap(Context context) {
			mBitmap = ReadBitMap(context, R.drawable.map);
			mBitMapWidth = mBitmap.getWidth();
			mBitMapHeight = mBitmap.getHeight();
			mWidthTileCount = mBitMapWidth / TILE_WIDTH;
			mHeightTileCount = mBitMapHeight / TILE_HEIGHT;
		}

		private void initAnimation(Context context) {
			// 这里可以用循环来处理总之我们需要把动画的ID传进去
			mHeroAnim[ANIM_DOWN] = new Animation(context, new int[] {
					R.drawable.hero_down_a, R.drawable.hero_down_b,
					R.drawable.hero_down_c, R.drawable.hero_down_d }, true);
			mHeroAnim[ANIM_LEFT] = new Animation(context, new int[] {
					R.drawable.hero_left_a, R.drawable.hero_left_b,
					R.drawable.hero_left_c, R.drawable.hero_left_d }, true);
			mHeroAnim[ANIM_RIGHT] = new Animation(context, new int[] {
					R.drawable.hero_right_a, R.drawable.hero_right_b,
					R.drawable.hero_right_c, R.drawable.hero_right_d }, true);
			mHeroAnim[ANIM_UP] = new Animation(context, new int[] {
					R.drawable.hero_up_a, R.drawable.hero_up_b,
					R.drawable.hero_up_c, R.drawable.hero_up_d }, true);
		}

		protected void Draw() {
			if (mCanvas == null)
				return;
			/** 绘制地图 **/
			DrawMap(mCanvas, mPaint, mBitmap);
			/** 绘制动画 **/
			RenderAnimation(mCanvas);
			/** 更新动画 **/
			UpdateHero();
			if (isBorderCollision) {
				DrawCollision(mCanvas, "碰撞");
			}

			if (isAcotrCollision) {
				DrawCollision(mCanvas, "碰撞");
			}
		}

		private void DrawCollision(Canvas canvas, String str) {
			drawRimString(canvas, str, Color.WHITE, mScreenWidth >> 1,
					mScreenHeight >> 1);
		}

		private void UpdateHero() {
			if (mAllkeyDown) {
				/** 根据按键更新显示动画 **/
				/** 在碰撞数组中寻找看自己是否与地图物理层发生碰撞 **/

				if (mIskeyDown) {
					mAnimationState = ANIM_DOWN;
					mHeroPosY += HERO_STEP;

					if (mHeroScreenY >= 320) {
						if (mHeroIndexY >= 10 && mHeroIndexY <= 20) {
							mMapPosY -= HERO_STEP;
						} else {
							mHeroScreenY += HERO_STEP;
						}
					} else {
						mHeroScreenY += HERO_STEP;
					}
				} else if (mIskeyLeft) {
					mAnimationState = ANIM_LEFT;
					mHeroPosX -= HERO_STEP;
					if (mHeroScreenX <= 96) {
						if (mHeroIndexX >= 3 && mHeroIndexX <= 7) {
							mMapPosX += HERO_STEP;
						} else {
							mHeroScreenX -= HERO_STEP;
						}
					} else {
						mHeroScreenX -= HERO_STEP;
					}
				} else if (mIskeyRight) {
					mAnimationState = ANIM_RIGHT;
					mHeroPosX += HERO_STEP;
					if (mHeroScreenX >= 192) {
						if (mHeroIndexX >= 6 && mHeroIndexX <= 10) {
							mMapPosX -= HERO_STEP;
						} else {
							mHeroScreenX += HERO_STEP;
						}
					} else {
						mHeroScreenX += HERO_STEP;
					}
				} else if (mIskeyUp) {
					mAnimationState = ANIM_UP;
					mHeroPosY -= HERO_STEP;

					if (mHeroScreenY <= 160) {
						if (mHeroIndexY >= 5 && mHeroIndexY <= 15) {
							mMapPosY += HERO_STEP;
						} else {
							mHeroScreenY -= HERO_STEP;
						}
					} else {
						mHeroScreenY -= HERO_STEP;
					}
				}
				/** 算出英雄移动后在地图二位数组中的索引 **/
				mHeroIndexX = mHeroPosX / TILE_WIDTH;
				mHeroIndexY = mHeroPosY / TILE_HEIGHT;

				// /** 检测人物是否出屏 **/
				isBorderCollision = false;
				if (mHeroPosX <= 0) {
					mHeroPosX = 0;
					mHeroScreenX = 0;
					isBorderCollision = true;
				} else if (mHeroPosX >= TILE_WIDTH_COUNT * TILE_WIDTH) {
					mHeroPosX = TILE_WIDTH_COUNT * TILE_WIDTH;
					mHeroScreenX = mScreenWidth;
					isBorderCollision = true;
				}
				if (mHeroPosY <= 0) {
					mHeroPosY = 0;
					mHeroScreenY = 0;
					isBorderCollision = true;
				} else if (mHeroPosY >= TILE_HEIGHT_COUNT * TILE_HEIGHT) {
					mHeroPosY = TILE_HEIGHT_COUNT * TILE_HEIGHT;

					mHeroScreenY = mScreenHeight;

					isBorderCollision = true;
				}
				// 防止地图越界
				if (mMapPosX >= 0) {
					mMapPosX = 0;
				} else if (mMapPosX <= -(480 - 320)) {
					mMapPosX = -(480 - 320);
				}
				if (mMapPosY >= 0) {
					mMapPosY = 0;
				} else if (mMapPosY <= -(800 - 480)) {
					mMapPosY = -(800 - 480);
				}

				/** 越界检测 **/
				int width = mCollision[0].length - 1;
				int height = mCollision.length - 1;

				if (mHeroIndexX <= 0) {
					mHeroIndexX = 0;
				} else if (mHeroIndexX >= width) {
					mHeroIndexX = width;
				}
				if (mHeroIndexY <= 0) {
					mHeroIndexY = 0;
				} else if (mHeroIndexY >= height) {
					mHeroIndexY = height;
				}

				// 碰撞检测
				if (mCollision[mHeroIndexY][mHeroIndexX] == -1) {
					mHeroPosX = mBackHeroPosX;
					mHeroPosY = mBackHeroPosY;
					mHeroScreenY = mBackHeroScreenY;
					mHeroScreenX = mBackHeroScreenX;
					isAcotrCollision = true;
				} else {
					mBackHeroPosX = mHeroPosX;
					mBackHeroPosY = mHeroPosY;
					mBackHeroScreenX = mHeroScreenX;
					mBackHeroScreenY = mHeroScreenY;
					isAcotrCollision = false;
				}

				Log.v("hero", "mHeroIndexX " + mHeroIndexX);
				Log.v("hero", "mHeroIndexY " + mHeroIndexY);

				mHeroImageX = mHeroScreenX - OFF_HERO_X;
				mHeroImageY = mHeroScreenY - OFF_HERO_Y;
			}
		}

		private void RenderAnimation(Canvas canvas) {

			if (mAllkeyDown) {
				/** 绘制主角动画 **/
				mHeroAnim[mAnimationState].DrawAnimation(canvas, mPaint,
						mHeroImageX, mHeroImageY);
			} else {
				/** 按键抬起后人物停止动画 **/
				mHeroAnim[mAnimationState].DrawFrame(canvas, mPaint,
						mHeroImageX, mHeroImageY, 0);
			}
		}

		/**
		 * 设置按键状态true为按下 false为抬起
		 * 
		 * @param keyCode
		 * @param state
		 */
		public void setKeyState(int keyCode, boolean state) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
				mIskeyDown = state;
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				mIskeyUp = state;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				mIskeyLeft = state;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				mIskeyRight = state;
				break;
			}
			mAllkeyDown = state;
		}

		public void setKeyStateV2(int keyCode, boolean state) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
				mIskeyDown = state;
				mIskeyUp = !state;
				mIskeyLeft = !state;
				mIskeyRight = !state;
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				mIskeyDown = !state;
				mIskeyUp = state;
				mIskeyLeft = !state;
				mIskeyRight = !state;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				mIskeyDown = !state;
				mIskeyUp = !state;
				mIskeyLeft = state;
				mIskeyRight = !state;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				mIskeyDown = !state;
				mIskeyUp = !state;
				mIskeyLeft = !state;
				mIskeyRight = state;
				break;
			}
			mAllkeyDown = state;
		}

		private void DrawMap(Canvas canvas, Paint paint, Bitmap bitmap) {
			int i, j;
			for (i = 0; i < TILE_HEIGHT_COUNT; i++) {
				for (j = 0; j < TILE_WIDTH_COUNT; j++) {
					int ViewID = mMapView[i][j];
					int ActorID = mMapAcotor[i][j];
					// 绘制地图第一层
					if (ViewID > TILE_NULL) {
						DrawMapTile(ViewID, canvas, paint, bitmap, mMapPosX
								+ (j * TILE_WIDTH), mMapPosY
								+ (i * TILE_HEIGHT));
					}
					// 绘制地图第二层
					if (ActorID > TILE_NULL) {
						DrawMapTile(ActorID, canvas, paint, bitmap, mMapPosX
								+ (j * TILE_WIDTH), mMapPosY
								+ (i * TILE_HEIGHT));
					}
				}
			}
		}

		/**
		 * 根据ID绘制一个tile块
		 * 
		 * @param id
		 * @param canvas
		 * @param paint
		 * @param bitmap
		 */
		private void DrawMapTile(int id, Canvas canvas, Paint paint,
				Bitmap bitmap, int x, int y) {
			// 根据数组中的ID算出在地图资源中的XY 坐标
			// 因为编辑器默认0 所以第一张tile的ID不是0而是1 所以这里 -1
			id--;
			int count = id / mWidthTileCount;
			int bitmapX = (id - (count * mWidthTileCount)) * TILE_WIDTH;
			int bitmapY = count * TILE_HEIGHT;
			DrawClipImage(canvas, paint, bitmap, x, y, bitmapX, bitmapY,
					TILE_WIDTH, TILE_HEIGHT);
		}

		/**
		 * 绘制图片中的一部分图片
		 * 
		 * @param canvas
		 * @param paint
		 * @param bitmap
		 * @param x
		 * @param y
		 * @param src_x
		 * @param src_y
		 * @param src_width
		 * @param src_Height
		 */
		private void DrawClipImage(Canvas canvas, Paint paint, Bitmap bitmap,
				int x, int y, int src_x, int src_y, int src_xp, int src_yp) {
			if (canvas == null)
				return;
			canvas.save();
			canvas.clipRect(x, y, x + src_xp, y + src_yp);
			canvas.drawBitmap(bitmap, x - src_x, y - src_y, paint);
			canvas.restore();
		}

		/**
		 * 程序切割图片
		 * 
		 * @param bitmap
		 * @param x
		 * @param y
		 * @param w
		 * @param h
		 * @return
		 */
		public Bitmap BitmapClipBitmap(Bitmap bitmap, int x, int y, int w, int h) {
			return Bitmap.createBitmap(bitmap, x, y, w, h);
		}

		/**
		 * 读取本地资源的图片
		 * 
		 * @param context
		 * @param resId
		 * @return
		 */
		public Bitmap ReadBitMap(Context context, int resId) {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			// 获取资源图片
			InputStream is = context.getResources().openRawResource(resId);
			return BitmapFactory.decodeStream(is, null, opt);
		}

		/**
		 * 绘制画带阴影的文字
		 * 
		 * @param canvas
		 * @param str
		 * @param color
		 * @param x
		 * @param y
		 */
		public final void drawRimString(Canvas canvas, String str, int color,
				int x, int y) {
			int backColor = mPaint.getColor();
			mPaint.setColor(~color);
			canvas.drawText(str, x + 1, y, mPaint);
			canvas.drawText(str, x, y + 1, mPaint);
			canvas.drawText(str, x - 1, y, mPaint);
			canvas.drawText(str, x, y - 1, mPaint);
			mPaint.setColor(color);
			canvas.drawText(str, x, y, mPaint);
			mPaint.setColor(backColor);
		}

		@Override
		public void run() {
			// while (mIsRunning) {
			// 在这里加上线程安全锁
			synchronized (mSurfaceHolder) {
				DrawMap();
			}
			// }
		}

		public void DrawMap() {
			mCanvas = mSurfaceHolder.lockCanvas();
			if (mCanvas != null) {
				Draw();
				/** 绘制结束后解锁显示在屏幕上 **/
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// surfaceView的大小发生改变的时候

		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			/** 启动游戏主线程 **/
			mIsRunning = true;
			mThread = new Thread(this);
			mThread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// surfaceView销毁的时候
			mIsRunning = false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		mAnimView.setKeyState(22, true);
		return super.onKeyDown(keyCode, event);
	}

	// @Override
	// public boolean onKeyUp(int keyCode, KeyEvent event) {
	// mAnimView.setKeyState(keyCode, false);
	// return super.onKeyUp(keyCode, event);
	// }

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("xb", "onDown--------------->>>>>>>>>>");
		return true;
	}

	int FLING_MIN_DISTANCE = 60;
	int FLING_MIN_VELOCITY = 1;

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.i("xb", "onFling--------------->>>>>>>>>>");
		// 参数解释：
		// e1：第1个ACTION_DOWN MotionEvent
		// e2：最后一个ACTION_MOVE MotionEvent
		// velocityX：X轴上的移动速度，像素/秒
		// velocityY：Y轴上的移动速度，像素/秒
		// 触发条件 ：
		// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			mAnimView.HERO_STEP = (int) Math.abs(e1.getX() - e2.getX());
			mAnimView.setKeyStateV2(KeyEvent.KEYCODE_DPAD_RIGHT, true);
			mAnimView.DrawMap();
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			mAnimView.HERO_STEP = (int) Math.abs(e1.getX() - e2.getX());
			mAnimView.setKeyStateV2(KeyEvent.KEYCODE_DPAD_LEFT, true);
			mAnimView.DrawMap();
		} else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
				&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
			mAnimView.HERO_STEP = (int) Math.abs(e1.getY() - e2.getY());
			mAnimView.setKeyStateV2(KeyEvent.KEYCODE_DPAD_DOWN, true);
			mAnimView.DrawMap();
		} else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
				&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
			mAnimView.HERO_STEP = (int) Math.abs(e1.getY() - e2.getY());
			mAnimView.setKeyStateV2(KeyEvent.KEYCODE_DPAD_UP, true);
			mAnimView.DrawMap();
		}

		return false;

	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return this.detector.onTouchEvent(event);
	}
}