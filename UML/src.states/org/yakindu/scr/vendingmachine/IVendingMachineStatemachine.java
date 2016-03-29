package org.yakindu.scr.vendingmachine;
import java.util.List;
import org.yakindu.scr.IStatemachine;
import org.yakindu.scr.ITimerCallback;

public interface IVendingMachineStatemachine extends ITimerCallback, IStatemachine {

	public interface SCInterface {
		public void raiseInsertPiece();
		public void raiseAddItem();
		public void raiseMaintenance();
		public void raiseAdd();
		public void raiseDelete();
		public void raiseLogin();
		public void raiseCreate();
		public void raiseLoad();
		public void raiseAlter();
		public void raiseSave();
		public void raiseLogout();
		public boolean isRaisedRefound();
		public long getPiece();
		public void setPiece(long value);
		public long getItemPrice();
		public void setItemPrice(long value);
		public long getLoginType();
		public void setLoginType(long value);
		public long getTotalPaid();
		public void setTotalPaid(long value);
		public List<SCInterfaceListener> getListeners();

		public void setSCInterfaceOperationCallback(SCInterfaceOperationCallback operationCallback);
	}

	public interface SCInterfaceListener {
		public void onRefoundRaised();
	}

	public interface SCInterfaceOperationCallback {
		public boolean validate();
	}

	public SCInterface getSCInterface();

}
