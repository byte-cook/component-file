package de.kobich.component.file;

import java.util.Comparator;

import de.kobich.commons.misc.extract.IText;


/**
 * Compares ITexts.
 * @author ckorn
 */
public class TextComparator implements Comparator<IText> {
	/**
	 * Constructor
	 */
	public TextComparator() {}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(IText o1, IText o2) {
		return o1.getText().compareTo(o2.getText());
	}

}
