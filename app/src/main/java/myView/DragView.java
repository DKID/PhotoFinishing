package myView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.example.gxl.photofinishing.R;

import Utils.ScreenUtils;

public class DragView extends LinearLayout {
    private int lastX;
    private int lastY;
    private Scroller mScroller;
    Context context;
    //Dragview��ʾ��ͼƬ
    Bitmap mBitmap;
    //Dragview�ƶ�״̬�ص�
    createFilelistener mlistener;
    //�������潨���ļ���Imageview���ĸ����������
    int[] pos = {-1, -1, -1, -1};
    //���������ƶ�����ǰ�ļ���Imageview���ĸ����������
    int[] chakanPos = {-1, -1, -1, -1};

    public void setMlistener(createFilelistener mlistener) {
        this.mlistener = mlistener;
    }

    public interface createFilelistener {
        //�����ƶ��Ի���
        void createFile();

        //���ƶ�ʱ���ı�����ɫ
        void betrue_createFile(int flag);

        //ɾ��������ƶ�view
        void remove_view();

        //�ƶ����Ѿ����õ��ļ�����
        void move_exist_file();
    }


    public DragView(Context context, Bitmap bitmap, int[] pos, int[] chakanPos) {
        this(context, null);
        this.mBitmap = bitmap;
        this.pos = pos;
        this.chakanPos = chakanPos;
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        this.context = context;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int x = (int) event.getX();
        int y = (int) event.getY();
        int Rawx = (int) event.getRawX();
        int Rawy = (int) event.getRawY();
        //����
        int top_left_x = Rawx - x;
        int top__left_y = Rawy - y;
        //����
        int top_right_x = top_left_x + width;
        int top_right_y = top__left_y;
        //����
        int botton_left_x = top_left_x;
        int botton_left_y = top__left_y + height;
        //����
        int botton_right_x = top_left_x + width;
        int botton_right_y = top__left_y + height;
        int imagepos[][] = {{top_left_x, top__left_y}, {top_right_x, top_right_y}, {botton_left_x, botton_left_y}, {botton_right_x, botton_right_y}};
        View viewGroup = (View) getParent();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offX = x - lastX;
                int offY = y - lastY;
                ((View) getParent()).scrollBy(-offX, -offY);
                if (panduan(pos, imagepos)) {
                    mlistener.betrue_createFile(1);
                } else {
                    mlistener.betrue_createFile(0);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("Rawx+Rawy", Rawx + " " + Rawy);
                Log.i("path", pos[0] + "#" + pos[1] + "#" + pos[2] + "#" + pos[3]);
                if (panduan(pos, imagepos)) {
                    mlistener.createFile();
                } else if (panduan(chakanPos, imagepos)) {
                    mlistener.move_exist_file();
                } else {
                    mlistener.remove_view();
                }
                break;
        }
        return true;
    }

    /**
     * �ж���ק��DragView�Ƿ�ͽ������ļ��л����ƶ������ļ����غ�
     *
     * @param pos
     * @param imagepos
     * @return
     */
    Boolean panduan(int[] pos, int[][] imagepos) {
        if ((pos[0] < imagepos[0][0] && imagepos[0][0] < pos[2]) && (pos[1] < imagepos[0][1] && imagepos[0][1] < pos[3])) {
            return true;
        }
        if ((pos[0] < imagepos[1][0] && imagepos[1][0] < pos[2]) && (pos[1] < imagepos[1][1] && imagepos[1][1] < pos[3])) {
            return true;
        }
        if ((pos[0] < imagepos[2][0] && imagepos[2][0] < pos[2]) && (pos[1] < imagepos[2][1] && imagepos[2][1] < pos[3])) {
            return true;
        }
        if ((pos[0] < imagepos[3][0] && imagepos[3][0] < pos[2]) && (pos[1] < imagepos[3][1] && imagepos[3][1] < pos[3])) {
            return true;
        }
        if (imagepos[0][0] < pos[0] && pos[0] < imagepos[1][0] && imagepos[0][1] < pos[1] && pos[1] < imagepos[2][1]) {
            return true;
        }
        if (imagepos[0][0] < pos[2] && pos[2] < imagepos[1][0] && imagepos[0][1] < pos[1] && pos[1] < imagepos[2][1]) {
            return true;
        }
        if (imagepos[0][0] < pos[0] && pos[0] < imagepos[1][0] && imagepos[0][1] < pos[3] && pos[3] < imagepos[2][1]) {
            return true;
        }
        if (imagepos[0][0] < pos[2] && pos[2] < imagepos[1][0] && imagepos[0][1] < pos[3] && pos[3] < imagepos[2][1]) {
            return true;
        }
        return false;
    }


    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(mScroller.getCurrX(),
                    mScroller.getCurrY());
        }
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View view = getChildAt(0);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Log.i("view", height + "height");
        Log.i("view", width + "width");
        Log.i("view", view.getMeasuredWidth() + ".getMeasuredWidth()");
        view.layout(ScreenUtils.dip2px(context, 6), ScreenUtils.dip2px(context, 2), width - ScreenUtils.dip2px(context, 6), height - ScreenUtils.dip2px(context, 5));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getmeasuredWidthSize(widthMeasureSpec),
                getmeasuredWidthSize(heightMeasureSpec));
        final ImageView view = new ImageView(context);
        view.setImageBitmap(mBitmap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ScreenUtils.dip2px(context, 100), ScreenUtils.dip2px(context, 100));
        addView(view, lp);
        measureChild(view, widthMeasureSpec, heightMeasureSpec);
        setBackgroundResource(R.drawable.image_unmove);
    }

    int getmeasuredWidthSize(int widthMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        Log.i("view", size + "size");
        return size;
    }

}
