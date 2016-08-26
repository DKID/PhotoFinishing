package customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.example.gxl.photofinishing.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import utils.LogUtils;
import utils.ScreenUtils;

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

    int top = 0;
    int left = 0;
    int first = 0;

    int changeToNormal = 0;//���淵������״̬
    int changeToCreate = 1;//�����ɴ���״̬:������֣���ɫ��ͷ��ʼ��˸
    int changeTobackground = 3;//toparea�ı�������ɫ
    int changeToDragview = 4;//Dragview��ʼ����

    /**
     * TopAreaFlag��ʾTopArea�Ƿ��ɫ
     */
    final int TopAreaNormal=1;
    final int TopAreaChange=2;
    int TopAreaFlag=TopAreaNormal;

    /**
     * �ǹ�׼���ô����ļ���
     */
    final int PrepareCreate=1;
    final int UnPrepareCreate=2;
    int CreateFileFlag=UnPrepareCreate;

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

        //ImageView��С�ͷŴ�
        void change_imageview(int flag);
    }


    public DragView(Context context, Bitmap bitmap, int[] pos, int[] chakanPos, int top, int left) {
        this(context, null);
        this.pos = pos;
        this.chakanPos = chakanPos;
        this.top = top;
        this.left = left;
        this.mBitmap = bitmap;
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        this.context = context;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        LogUtils.loggxl("zuobiao" + x + " " + y);
        int Rawx = (int) event.getRawX();
        int Rawy = (int) event.getRawY();
        //����
        int top_left_x = this.getLeft();
        int top__left_y = this.getTop();
        //����
        int top_right_x = this.getRight();
        int top_right_y = this.getTop();
        //����
        int botton_left_x = this.getLeft();
        int botton_left_y = this.getBottom();
        //����
        int botton_right_x = this.getRight();
        int botton_right_y = this.getBottom();
        int Width = this.getRight() - this.getLeft();
        int Height = this.getBottom() - this.getTop();
        LogUtils.loggxl("Width+Height" + width + height);
        //����
        top_left_x = this.getLeft() + (int) (0.2 * Width);
        top__left_y = this.getTop() + (int) (0.2 * Height);
        //����
        top_right_x = this.getRight() - (int) (Width * 0.2);
        top_right_y = this.getTop() + (int) (0.2 * Height);
        //����
        botton_left_x = this.getLeft() + (int) (0.2 * Width);
        botton_left_y = this.getBottom() - (int) (0.2 * Height);
        //����
        botton_right_x = this.getRight() - (int) (Width * 0.2);
        botton_right_y = this.getBottom() - (int) (0.2 * Height);
        int imagepos[][] = {{top_left_x, top__left_y}, {top_right_x, top_right_y}, {botton_left_x, botton_left_y}, {botton_right_x, botton_right_y}};
        LogUtils.loggxl("juxing" + top_left_x + " " + top__left_y + " " + botton_right_x + " " + botton_right_y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                LogUtils.loggxl("lastX+lastY" + lastX + lastY);
                break;
            case MotionEvent.ACTION_MOVE:
                if (first == 0) {
                    first = 1;
                    lastX = x;
                    lastY = y;
                    return true;
                }
                setBackgroundResource(R.drawable.image_move);
                int offX = x - lastX;
                int offY = y - lastY;
                LogUtils.loggxl("offX+oFFY" + offX + offX);
                lastX = x;
                lastY = y;
                layout(getLeft() + offX, getTop() + offY, getRight() + offX, getBottom() + offY);
                if (getTop() < top&&TopAreaFlag!=TopAreaChange) {
                    mlistener.change_imageview(changeTobackground);
                    TopAreaFlag=TopAreaChange;
                } else if(getTop()>=top&&TopAreaFlag!=TopAreaNormal){
                    mlistener.change_imageview(changeToNormal);
                    TopAreaFlag=TopAreaNormal;
                }
                int imagepos2[][] = {{top_left_x, top__left_y}, {top_right_x, top_right_y}, {botton_left_x, botton_left_y}, {botton_right_x, botton_right_y}};
                Log.i("movepath", top_left_x + "#" + top__left_y + "#" + top_right_x + "#" + top_right_y);
                if (panduan(pos, imagepos2)&&CreateFileFlag!=PrepareCreate) {
                    mlistener.betrue_createFile(1);
                    CreateFileFlag=PrepareCreate;
                } else if(CreateFileFlag!=UnPrepareCreate&&(!panduan(pos, imagepos2))){
                    mlistener.betrue_createFile(0);
                    CreateFileFlag=UnPrepareCreate;
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
                    mlistener.change_imageview(changeToNormal);
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


    public Boolean isNeedmove(int rawX, int rawY, int left, int right, int top, int bottom) {
        if (left < rawX && rawX < right && top < rawY && rawY < bottom)
            return true;
        else
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
        Log.i("customview", height + "height");
        Log.i("customview", width + "width");
        Log.i("customview", view.getMeasuredWidth() + ".getMeasuredWidth()");
        view.layout(ScreenUtils.dip2px(context, 6), ScreenUtils.dip2px(context, 2), width - ScreenUtils.dip2px(context, 6), height - ScreenUtils.dip2px(context, 5));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getmeasuredWidthSize(widthMeasureSpec),
                getmeasuredWidthSize(heightMeasureSpec));
        final ImageView view = new ImageView(context);
        view.setImageBitmap(mBitmap);
        addView(view);
        setBackgroundResource(R.drawable.image_unmove);
    }

    int getmeasuredWidthSize(int widthMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        Log.i("customview", size + "size");
        return size;
    }

}
