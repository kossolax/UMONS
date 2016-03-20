package org.yakindu.scr.defaultsm;
import org.yakindu.scr.IStatemachine;
import org.yakindu.scr.ITimerCallback;

public interface IDefaultSMStatemachine extends ITimerCallback, IStatemachine {

	public interface SCInterface {
		public void raiseInsertPiece();
		public void raiseAddItem();
		public void raiseRefound();
		public void raiseMaintenance();
		public double getPiece();
		public void setPiece(double value);
		public double getItemPrice();
		public void setItemPrice(double value);

	}

	public SCInterface getSCInterface();

}
