package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferReaderCallback {
	public Integer doSometingWithReaderInteger(BufferedReader br) throws IOException;
}
