package com.patternity.graphic.layout;

import com.patternity.graphic.Position;
import com.patternity.graphic.dag.Node;

public interface Layout {

	Position position(Node node);

}