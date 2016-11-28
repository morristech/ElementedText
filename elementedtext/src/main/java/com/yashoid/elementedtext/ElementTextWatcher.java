package com.yashoid.elementedtext;

import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;

public abstract class ElementTextWatcher implements TextWatcher {

	private ElementSpan[] mSpans;

	private int mNewSpanSearchStart;
	private int mNewSpanSearchEnd;

	@Override
	public void afterTextChanged(Editable s) {
		for (ElementSpan span: mSpans) {
			int start = s.getSpanStart(span);
			int end = s.getSpanEnd(span);

			if (start==-1 || end==-1) {
				continue;
			}
			
			if (!s.subSequence(start, end).toString().equals(span.getText().toString())) {
				s.removeSpan(span);
				span.onRemoved();
				onElementRemoved(span);
			}
		}

		String text = s.toString();

		int start = text.substring(0, mNewSpanSearchStart).lastIndexOf(" ");
		if (start==-1) {
			start = 0;
		}

		int end = text.indexOf(" ", mNewSpanSearchEnd);
		if (end==-1) {
			end = text.length();
		}

		int index = start;

		while (index<end && index!=-1) {
			ElementQueryResult result = findFirstElement(text, index);
			
			if (result!=null) {
				index = result.index;
			}
			else {
				index = -1;
			}

			if (index!=-1 && index<end) {
				ElementSpan[] spans = s.getSpans(index, index + result.result.length(), ElementSpan.class);

				if (spans.length==0) {
					s.setSpan(createElement(result), index, index + result.result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}

				index++;
			}
		}
	}
	
	/**
	 * 
	 * @param s
	 * @return null if no element was found.
	 */
	abstract protected ElementQueryResult findFirstElement(String s, int start);
	
	abstract protected ElementSpan createElement(ElementQueryResult queryResult);
	
	protected void onElementRemoved(ElementSpan element) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		Spanned spanned = (Spanned) s;

		mSpans = spanned.getSpans(start, start + count, ElementSpan.class);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mNewSpanSearchStart = start;
		mNewSpanSearchEnd = start + count;
	}
	
	static public class ElementQueryResult {

		public final CharSequence result;
		public final int index;
		
		public ElementQueryResult(CharSequence result, int index) {
			this.result = result;
			this.index = index;
		}
		
	}

}
