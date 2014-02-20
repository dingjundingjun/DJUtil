package com.dj.util.views;

import java.util.ArrayList;

import jding.debug.JDingDebug;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

public class GridAnimationView extends GridView
{
	private final String TAG = "GridAnimationView";
	private boolean bShortAnimation = false;
	private ArrayList<View> mArrayList = new ArrayList<View>();
	private int mSelection = 5;
	public GridAnimationView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void attachLayoutAnimationParameters(View child,
			android.view.ViewGroup.LayoutParams params, int index, int count)
	{
		JDingDebug.printfD(TAG, "attachLayoutAnimationParameters:" + index + " " + count);
		super.attachLayoutAnimationParameters(child, params, index, count);
	}

	@Override
	protected int computeVerticalScrollExtent()
	{
		// TODO Auto-generated method stub
		return super.computeVerticalScrollExtent();
	}

	@Override
	protected int computeVerticalScrollOffset()
	{
		// TODO Auto-generated method stub
		return super.computeVerticalScrollOffset();
	}

	@Override
	protected int computeVerticalScrollRange()
	{
		// TODO Auto-generated method stub
		return super.computeVerticalScrollRange();
	}

	@Override
	public ListAdapter getAdapter()
	{
		// TODO Auto-generated method stub
		return super.getAdapter();
	}

	@Override
	public int getColumnWidth()
	{
		// TODO Auto-generated method stub
		return super.getColumnWidth();
	}

	@Override
	public int getGravity()
	{
		// TODO Auto-generated method stub
		return super.getGravity();
	}

	@Override
	public int getHorizontalSpacing()
	{
		// TODO Auto-generated method stub
		return super.getHorizontalSpacing();
	}

	@Override
	@ExportedProperty
	public int getNumColumns()
	{
		// TODO Auto-generated method stub
		return super.getNumColumns();
	}

	@Override
	public int getRequestedColumnWidth()
	{
		// TODO Auto-generated method stub
		return super.getRequestedColumnWidth();
	}

	@Override
	public int getRequestedHorizontalSpacing()
	{
		// TODO Auto-generated method stub
		return super.getRequestedHorizontalSpacing();
	}

	@Override
	public int getStretchMode()
	{
		// TODO Auto-generated method stub
		return super.getStretchMode();
	}

	@Override
	public int getVerticalSpacing()
	{
		// TODO Auto-generated method stub
		return super.getVerticalSpacing();
	}

	@Override
	protected void layoutChildren()
	{
		final boolean blockLayoutRequests = mBlockLayoutRequests;
		if (!blockLayoutRequests) 
		{
		mBlockLayoutRequests = true;
		}
		1133 
		1134         try {
		1135             super.layoutChildren();
		1136 
		1137             invalidate();
		1138 
		1139             if (mAdapter == null) {
		1140                 resetList();
		1141                 invokeOnItemScrollListener();
		1142                 return;
		1143             }
		1144 
		1145             final int childrenTop = mListPadding.top;
		1146             final int childrenBottom = mBottom - mTop - mListPadding.bottom;
		1147 
		1148             int childCount = getChildCount();
		1149             int index;
		1150             int delta = 0;
		1151 
		1152             View sel;
		1153             View oldSel = null;
		1154             View oldFirst = null;
		1155             View newSel = null;
		1156 
		1157             // Remember stuff we will need down below
		1158             switch (mLayoutMode) {
		1159             case LAYOUT_SET_SELECTION:
		1160                 index = mNextSelectedPosition - mFirstPosition;
		1161                 if (index >= 0 && index < childCount) {
		1162                     newSel = getChildAt(index);
		1163                 }
		1164                 break;
		1165             case LAYOUT_FORCE_TOP:
		1166             case LAYOUT_FORCE_BOTTOM:
		1167             case LAYOUT_SPECIFIC:
		1168             case LAYOUT_SYNC:
		1169                 break;
		1170             case LAYOUT_MOVE_SELECTION:
		1171                 if (mNextSelectedPosition >= 0) {
		1172                     delta = mNextSelectedPosition - mSelectedPosition;
		1173                 }
		1174                 break;
		1175             default:
		1176                 // Remember the previously selected view
		1177                 index = mSelectedPosition - mFirstPosition;
		1178                 if (index >= 0 && index < childCount) {
		1179                     oldSel = getChildAt(index);
		1180                 }
		1182                 // Remember the previous first child
		1183                 oldFirst = getChildAt(0);
		1184             }
		1185 
		1186             boolean dataChanged = mDataChanged;
		1187             if (dataChanged) {
		1188                 handleDataChanged();
		1189             }
		1190 
		1191             // Handle the empty set by removing all views that are visible
		1192             // and calling it a day
		1193             if (mItemCount == 0) {
		1194                 resetList();
		1195                 invokeOnItemScrollListener();
		1196                 return;
		1197             }
		1198 
		1199             setSelectedPositionInt(mNextSelectedPosition);
		1200 
		1201             // Pull all children into the RecycleBin.
		1202             // These views will be reused if possible
		1203             final int firstPosition = mFirstPosition;
		1204             final RecycleBin recycleBin = mRecycler;
		1205 
		1206             if (dataChanged) {
		1207                 for (int i = 0; i < childCount; i++) {
		1208                     recycleBin.addScrapView(getChildAt(i), firstPosition+i);
		1209                 }
		1210             } else {
		1211                 recycleBin.fillActiveViews(childCount, firstPosition);
		1212             }
		1213 
		1214             // Clear out old views
		1215             //removeAllViewsInLayout();
		1216             detachAllViewsFromParent();
		1217             recycleBin.removeSkippedScrap();
		1218 
		1219             switch (mLayoutMode) {
		1220             case LAYOUT_SET_SELECTION:
		1221                 if (newSel != null) {
		1222                     sel = fillFromSelection(newSel.getTop(), childrenTop, childrenBottom);
		1223                 } else {
		1224                     sel = fillSelection(childrenTop, childrenBottom);
		1225                 }
		1226                 break;
		1227             case LAYOUT_FORCE_TOP:
		1228                 mFirstPosition = 0;
		1229                 sel = fillFromTop(childrenTop);
		1230                 adjustViewsUpOrDown();
		   break;
		   1232             case LAYOUT_FORCE_BOTTOM:
		   1233                 sel = fillUp(mItemCount - 1, childrenBottom);
		   1234                 adjustViewsUpOrDown();
		   1235                 break;
		   1236             case LAYOUT_SPECIFIC:
		   1237                 sel = fillSpecific(mSelectedPosition, mSpecificTop);
		   1238                 break;
		   1239             case LAYOUT_SYNC:
		   1240                 sel = fillSpecific(mSyncPosition, mSpecificTop);
		   1241                 break;
		   1242             case LAYOUT_MOVE_SELECTION:
		   1243                 // Move the selection relative to its old position
		   1244                 sel = moveSelection(delta, childrenTop, childrenBottom);
		   1245                 break;
		   1246             default:
		   1247                 if (childCount == 0) {
		   1248                     if (!mStackFromBottom) {
		   1249                         setSelectedPositionInt(mAdapter == null || isInTouchMode() ?
		   1250                                 INVALID_POSITION : 0);
		   1251                         sel = fillFromTop(childrenTop);
		   1252                     } else {
		   1253                         final int last = mItemCount - 1;
		   1254                         setSelectedPositionInt(mAdapter == null || isInTouchMode() ?
		   1255                                 INVALID_POSITION : last);
		   1256                         sel = fillFromBottom(last, childrenBottom);
		   1257                     }
		   1258                 } else {
		   1259                     if (mSelectedPosition >= 0 && mSelectedPosition < mItemCount) {
		   1260                         sel = fillSpecific(mSelectedPosition, oldSel == null ?
		   1261                                 childrenTop : oldSel.getTop());
		   1262                     } else if (mFirstPosition < mItemCount)  {
		   1263                         sel = fillSpecific(mFirstPosition, oldFirst == null ?
		   1264                                 childrenTop : oldFirst.getTop());
		   1265                     } else {
		   1266                         sel = fillSpecific(0, childrenTop);
		   1267                     }
		   1268                 }
		   1269                 break;
		   1270             }
		   1271 
		   1272             // Flush any cached views that did not get reused above
		   1273             recycleBin.scrapActiveViews();
		   1274 
		   1275             if (sel != null) {
		   1276                positionSelector(INVALID_POSITION, sel);
		   1277                mSelectedTop = sel.getTop();
		   1278             } else if (mTouchMode > TOUCH_MODE_DOWN && mTouchMode < TOUCH_MODE_SCROLL) {
		   1279                 View child = getChildAt(mMotionPosition - mFirstPosition);
		   1280                 if (child != null) positionSelector(mMotionPosition, child);
		   1281             } else {
		   1282                 mSelectedTop = 0;
		   1283                 mSelectorRect.setEmpty();
		   }
		   1285 
		   1286             mLayoutMode = LAYOUT_NORMAL;
		   1287             mDataChanged = false;
		   1288             if (mPositionScrollAfterLayout != null) {
		   1289                 post(mPositionScrollAfterLayout);
		   1290                 mPositionScrollAfterLayout = null;
		   1291             }
		   1292             mNeedSync = false;
		   1293             setNextSelectedPositionInt(mSelectedPosition);
		   1294 
		   1295             updateScrollIndicators();
		   1296 
		   1297             if (mItemCount > 0) {
		   1298                 checkSelectionChanged();
		   1299             }
		   1300 
		   1301             invokeOnItemScrollListener();
		   1302         } finally {
		   1303             if (!blockLayoutRequests) {
		   1304                 mBlockLayoutRequests = false;
		   1305             }
		   1306         }

		//在 这要弄一个动画  动画完成重新排序
//		if(!bUseAnimation)
//		int changeBeforFirstPosition = getFirstVisiblePosition();
//		int firstLayout = mSelection - 2;
//		mArrayList.clear();
//		for(int i = 0;i < 16;i++)    //把当前显示的view保存起来
//		{
//			mArrayList.add(getChildAt(i));
//		}
////		JDingDebug.printfD(TAG," getFirstVisiblePosition():" + getFirstVisiblePosition());    //变换以前第一个VIEW位置
//		if(!bShortAnimation)
//		{
//			super.layoutChildren();
//		}
//		if(bShortAnimation)
//		{
//			int firstViewPosition = getFirstVisiblePosition();    //第一个view位置
//			JDingDebug.printfD(TAG, "layoutChildren:");
//			int childCount = getChildCount();
//			RotateAnimation rotateAnimation = new RotateAnimation(0, 90);
//			TranslateAnimation translateAnimation = new TranslateAnimation(0, -50, 0, 0);
//			AnimationSet animationSet = new AnimationSet(false);
//			animationSet.addAnimation(rotateAnimation);
//			animationSet.addAnimation(translateAnimation);
//			animationSet.setDuration(3000);
//			for(int i = 0;i < 16;i++)
//			{
//				View tempView = mArrayList.get(i);
//				if(i < firstLayout)
//				{
//				}
//				
//				
//				
//				if(i % 2 == 0)
//					tempView.setRotation(60);
//				else
//					tempView.setRotation(-60);
//			}
//				
//			for(int i = 0; i < childCount; i++)
//			{
//				View child = getChildAt(i);
//				JDingDebug.printfD(TAG,
//						"index:" + i + " left:" + child.getLeft() + " top:"
//								+ child.getTop() + " width:" + child.getWidth()
//								+ " height:" + child.getHeight() + " getFirstVisiblePosition():" + getFirstVisiblePosition());
//				/*android.view.ViewGroup.LayoutParams params = child.getLayoutParams();*/
//	//			child.setAnimation(animationSet);
//				if(i % 2 == 0)
//					child.setRotation(60);
//				else
//					child.setRotation(-60);
//			}
		}
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect)
	{
		// TODO Auto-generated method stub
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}

	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event)
	{
		// TODO Auto-generated method stub
		super.onInitializeAccessibilityEvent(event);
	}

	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info)
	{
		// TODO Auto-generated method stub
		super.onInitializeAccessibilityNodeInfo(info);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event)
	{
		// TODO Auto-generated method stub
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void setAdapter(ListAdapter adapter)
	{
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
	}

	@Override
	public void setColumnWidth(int columnWidth)
	{
		// TODO Auto-generated method stub
		super.setColumnWidth(columnWidth);
	}

	@Override
	public void setGravity(int gravity)
	{
		// TODO Auto-generated method stub
		super.setGravity(gravity);
	}

	@Override
	public void setHorizontalSpacing(int horizontalSpacing)
	{
		// TODO Auto-generated method stub
		super.setHorizontalSpacing(horizontalSpacing);
	}

	@Override
	public void setNumColumns(int numColumns)
	{
		// TODO Auto-generated method stub
		super.setNumColumns(numColumns);
	}

	@Override
	public void setRemoteViewsAdapter(Intent intent)
	{
		// TODO Auto-generated method stub
		super.setRemoteViewsAdapter(intent);
	}

	@Override
	public void setSelection(int position)
	{
		// TODO Auto-generated method stub
		super.setSelection(position);
	}

	@Override
	public void setStretchMode(int stretchMode)
	{
		// TODO Auto-generated method stub
		super.setStretchMode(stretchMode);
	}

	@Override
	public void setVerticalSpacing(int verticalSpacing)
	{
		// TODO Auto-generated method stub
		super.setVerticalSpacing(verticalSpacing);
	}

	@Override
	public void smoothScrollByOffset(int offset)
	{
		// TODO Auto-generated method stub
		super.smoothScrollByOffset(offset);
	}

	@Override
	public void smoothScrollToPosition(int position)
	{
		// TODO Auto-generated method stub
		super.smoothScrollToPosition(position);
	}
	
	public void setChange(boolean b)
	{
		bShortAnimation = b;
	}
}
