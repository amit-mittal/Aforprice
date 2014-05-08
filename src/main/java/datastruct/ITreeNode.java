package datastruct;

import java.util.List;

public interface ITreeNode<T> {
	ITreeNode<T> getParent();
	List<ITreeNode<T>> getChildren();
	boolean isTerminal();
	T getData();
	int getLevel();
}
