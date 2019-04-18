package com.exadel.exc;

import static com.exadel.exc.Utils.*;

public class Line {

	public int lno;
    public String text;
    public boolean match;

    public Line(int lno, String text, boolean match) {
		this.lno = lno;
		this.text = xmp(text);
		this.match = match;
	}
    
}
