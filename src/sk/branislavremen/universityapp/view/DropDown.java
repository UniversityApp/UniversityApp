package sk.branislavremen.universityapp.view;

import java.lang.reflect.Method;

import sk.branislavremen.universityapp.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DropDown extends RelativeLayout implements OnClickListener,
		OnItemClickListener {

	public static int DEFAULT_TITLE_COLOR = 0xFF000000;
	public static int DEFAULT_PADDING_SIZE = 10;

	ListView list;
	TextView title;
	ImageView arrow;

	String titleText;
	int titleColor;
	CharSequence[] items;

	Context context;
	int lastClicked = -1;
	boolean hidden = true;

	public DropDown(Context context) {
		this(context, null);
	}

	public DropDown(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.DropDown, 0, 0);

		Drawable arrowImage = a.getDrawable(R.styleable.DropDown_arrowImage);
		titleText = a.getString(R.styleable.DropDown_titleText);
		titleColor = a.getColor(R.styleable.DropDown_titleColor,
				DEFAULT_TITLE_COLOR);
		Drawable titleBackground = a
				.getDrawable(R.styleable.DropDown_titleBackground);
		items = a.getTextArray(R.styleable.DropDown_android_entries);

		a.recycle();

		setGravity(Gravity.CENTER_VERTICAL);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_drop_down, this, true);

		title = (TextView) getChildAt(0);
		title.setText(titleText);
		title.setOnClickListener(this);
		title.setTextColor(titleColor);
		if (titleBackground != null) {
			title.setBackgroundDrawable(titleBackground);
		} else {
			title.setBackgroundResource(R.drawable.dropdown_background);
		}
		title.setPadding(DEFAULT_PADDING_SIZE, DEFAULT_PADDING_SIZE,
				DEFAULT_PADDING_SIZE * 5, DEFAULT_PADDING_SIZE);

		arrow = (ImageView) getChildAt(1);
		if (arrowImage != null) {
			arrow.setImageDrawable(arrowImage);
		}
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_TOP, R.id.dropDown_title);
		lp.addRule(RelativeLayout.ALIGN_RIGHT, R.id.dropDown_title);
		// TODO: Set margin according to the arrow images actual size
		lp.setMargins(0, 20, 10, 0);
		arrow.setLayoutParams(lp);

		list = (ListView) getChildAt(2);
		list.setOnItemClickListener(this);
		if (items != null) {
			list.setAdapter(new ArrayAdapter<CharSequence>(context,
					android.R.layout.simple_list_item_1, items));
		}
		list.setBackgroundColor(getResources().getColor(
				R.color.dropdown_list_background_green));
		
		list.setDivider(new PaintDrawable(getResources().getColor(
				R.color.dropdown_list_divider_blue)));
		list.setDividerHeight(4);
		// Hide the list
		list.getLayoutParams().height = 0;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());

		bundle.putInt("lastClicked", this.lastClicked);

		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;

			this.lastClicked = bundle.getInt("lastClicked");
			setSelection(lastClicked);

			super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
			return;
		}

		super.onRestoreInstanceState(state);
	}

	public void onClick(View v) {
		Animation anim = DropDown.expand(list);
		list.startAnimation(anim);

		rotateArrow();

		hidden = !hidden;

	}

	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		Animation anim = DropDown.expand(list);
		list.startAnimation(anim);
		setSelection(position);

		rotateArrow();

		hidden = !hidden;
	}

	private void rotateArrow() {
		RotateAnimation rotate;
		if (hidden) {
			rotate = new RotateAnimation(0, 90, 0, arrow.getWidth() / 2, 0,
					arrow.getHeight() / 2);

		} else {
			rotate = new RotateAnimation(90, 0, 0, arrow.getWidth() / 2, 0,
					arrow.getHeight() / 2);
		}

		rotate.setDuration(ANIMATION_SPEED);
		rotate.setFillAfter(true);
		rotate.setRepeatCount(0);
		arrow.startAnimation(rotate);
	}

	public void setOnItemSelectedListener(
			OnItemSelectedListener onItemSelectedListener) {
		list.setOnItemSelectedListener(onItemSelectedListener);
	}

	public OnItemSelectedListener getOnItemSelectedListener() {
		return list.getOnItemSelectedListener();
	}

	public void setTitle(String titleString) {
		title.setText(titleString);
		this.titleText = titleString;
	}

	public void setTitle(int resId) {
		this.setTitle(this.context.getString(resId));
	}

	public void setTitleColor(int color) {
		title.setTextColor(color);
	}

	public void setListBackgroundColor(int color) {
		list.setBackgroundColor(color);
	}

	public void setItems(String[] listItems) {
		this.items = listItems;
		list.setAdapter(new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, listItems));
		lastClicked = -1;
		setSelection(-1);
	}

	public void setItems(int resId) {
		this.setItems(context.getResources().getStringArray(resId));
	}

	public void setSelection(int position) {
		lastClicked = position;
		if (position == -1) {
			title.setText(titleText);
		} else {
			title.setText(getSelectedItemString());
		}
	}

	public int getSelectedItemPosition() {
		return lastClicked;
	}
	
	public Object getSelectedItem() {
		if (lastClicked < 0 || lastClicked > list.getAdapter().getCount()) {
			return null;
		}

		return list.getAdapter().getItem(lastClicked);
	}

	public String getSelectedItemString() {
		Object item = getSelectedItem();

		if (item != null) {
			return item.toString();
		} else {
			return null;
		}
	}

	private static final long ANIMATION_SPEED = 500;

	public static Animation expand(final View v) {
		final boolean expand = (v.getLayoutParams().height == 0 || v
				.getVisibility() == View.GONE) ? true : false;

		try {
			Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
					int.class);
			m.setAccessible(true);
			m.invoke(v,
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(
							((View) v.getParent()).getMeasuredWidth(),
							MeasureSpec.AT_MOST));
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int initialHeight = (v.getClass() == ListView.class) ? getListViewHeightBasedOnChildren((ListView) v)
				: v.getMeasuredHeight();

		if (expand) {
			v.getLayoutParams().height = 0;
		} else {
			v.getLayoutParams().height = initialHeight;
		}

		v.setVisibility(View.VISIBLE);
		// !!!!!!!!!!!!!This is the secret of the templars. Without this line,
		// on pre 4.0 devices the animation cannot be seen!!!!!!!!!!
		((View) v.getParent()).invalidate();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				int newHeight = 0;
				if (expand) {
					newHeight = (int) (initialHeight * interpolatedTime);
				} else {
					newHeight = (int) (initialHeight * (1 - interpolatedTime));
				}
				v.getLayoutParams().height = newHeight;
				v.requestLayout();
				if (interpolatedTime == 1) {
					if (!expand) {
						v.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		a.setDuration(ANIMATION_SPEED);
		return a;
	}

	public static int getListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return 0;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		return totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	}
}
