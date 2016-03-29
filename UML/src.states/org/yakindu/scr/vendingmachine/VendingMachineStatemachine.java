package org.yakindu.scr.vendingmachine;
import org.yakindu.scr.ITimer;

public class VendingMachineStatemachine implements IVendingMachineStatemachine {

	protected class SCInterfaceImpl implements SCInterface {

		private boolean insertPiece;

		public void raiseInsertPiece() {
			insertPiece = true;
		}

		private boolean addItem;

		public void raiseAddItem() {
			addItem = true;
		}

		private boolean refound;

		public void raiseRefound() {
			refound = true;
		}

		private boolean maintenance;

		public void raiseMaintenance() {
			maintenance = true;
		}

		private boolean add;

		public void raiseAdd() {
			add = true;
		}

		private boolean delete;

		public void raiseDelete() {
			delete = true;
		}

		private boolean login;

		public void raiseLogin() {
			login = true;
		}

		private boolean create;

		public void raiseCreate() {
			create = true;
		}

		private boolean load;

		public void raiseLoad() {
			load = true;
		}

		private boolean alter;

		public void raiseAlter() {
			alter = true;
		}

		private boolean save;

		public void raiseSave() {
			save = true;
		}

		private boolean logout;

		public void raiseLogout() {
			logout = true;
		}

		private long piece;

		public long getPiece() {
			return piece;
		}

		public void setPiece(long value) {
			this.piece = value;
		}

		private long itemPrice;

		public long getItemPrice() {
			return itemPrice;
		}

		public void setItemPrice(long value) {
			this.itemPrice = value;
		}

		private long loginType;

		public long getLoginType() {
			return loginType;
		}

		public void setLoginType(long value) {
			this.loginType = value;
		}

		private long totalPaid;

		public long getTotalPaid() {
			return totalPaid;
		}

		public void setTotalPaid(long value) {
			this.totalPaid = value;
		}

		protected void clearEvents() {
			insertPiece = false;
			addItem = false;
			refound = false;
			maintenance = false;
			add = false;
			delete = false;
			login = false;
			create = false;
			load = false;
			alter = false;
			save = false;
			logout = false;
		}

	}

	protected SCInterfaceImpl sCInterface;

	private boolean initialized = false;

	public enum State {
		machine_Maintenance, machine_Maintenance_r1_Authentification, machine_Maintenance_r1_Stock, machine_Maintenance_r1_MachineManagement, machine_Maintenance_r1_CreateMachine, machine_Maintenance_r1_moduleManagement, machine_Maintenance_r1_ArticleManagement, machine_Machine, machine_Machine_r1_Standby, machine_Machine_r1_Selection, machine_Machine_r1_Distribute, coin_ATM_Standby, coin_ATM_Pay, coin_ATM_Withdraw, $NullState$
	};

	private final State[] stateVector = new State[2];

	private int nextStateIndex;

	private ITimer timer;

	private final boolean[] timeEvents = new boolean[2];

	private long needMoney;

	protected void setNeedMoney(long value) {
		needMoney = value;
	}

	protected long getNeedMoney() {
		return needMoney;
	}

	private long totalMoney;

	protected void setTotalMoney(long value) {
		totalMoney = value;
	}

	protected long getTotalMoney() {
		return totalMoney;
	}

	public VendingMachineStatemachine() {

		sCInterface = new SCInterfaceImpl();
	}

	public void init() {
		this.initialized = true;
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		for (int i = 0; i < 2; i++) {
			stateVector[i] = State.$NullState$;
		}

		clearEvents();
		clearOutEvents();

		sCInterface.setPiece(1);

		sCInterface.setItemPrice(1);

		sCInterface.setLoginType(0);

		sCInterface.setTotalPaid(0);

		setNeedMoney(0);

		setTotalMoney(0);
	}

	public void enter() {
		if (!initialized)
			throw new IllegalStateException(
					"The statemachine needs to be initialized first by calling the init() function.");

		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		enterSequence_Machine_default();

		enterSequence_Coin_ATM_default();
	}

	public void exit() {
		exitSequence_Machine();

		exitSequence_Coin_ATM();
	}

	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {

		return stateVector[0] != State.$NullState$ || stateVector[1] != State.$NullState$;
	}

	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	 * @see IStatemachine#isFinal() 
	 */
	public boolean isFinal() {
		return false;
	}

	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCInterface.clearEvents();

		for (int i = 0; i < timeEvents.length; i++) {
			timeEvents[i] = false;
		}
	}

	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
	}

	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
		switch (state) {
			case machine_Maintenance :
				return stateVector[0].ordinal() >= State.machine_Maintenance.ordinal()
						&& stateVector[0].ordinal() <= State.machine_Maintenance_r1_ArticleManagement.ordinal();
			case machine_Maintenance_r1_Authentification :
				return stateVector[0] == State.machine_Maintenance_r1_Authentification;
			case machine_Maintenance_r1_Stock :
				return stateVector[0] == State.machine_Maintenance_r1_Stock;
			case machine_Maintenance_r1_MachineManagement :
				return stateVector[0] == State.machine_Maintenance_r1_MachineManagement;
			case machine_Maintenance_r1_CreateMachine :
				return stateVector[0] == State.machine_Maintenance_r1_CreateMachine;
			case machine_Maintenance_r1_moduleManagement :
				return stateVector[0] == State.machine_Maintenance_r1_moduleManagement;
			case machine_Maintenance_r1_ArticleManagement :
				return stateVector[0] == State.machine_Maintenance_r1_ArticleManagement;
			case machine_Machine :
				return stateVector[0].ordinal() >= State.machine_Machine.ordinal()
						&& stateVector[0].ordinal() <= State.machine_Machine_r1_Distribute.ordinal();
			case machine_Machine_r1_Standby :
				return stateVector[0] == State.machine_Machine_r1_Standby;
			case machine_Machine_r1_Selection :
				return stateVector[0] == State.machine_Machine_r1_Selection;
			case machine_Machine_r1_Distribute :
				return stateVector[0] == State.machine_Machine_r1_Distribute;
			case coin_ATM_Standby :
				return stateVector[1] == State.coin_ATM_Standby;
			case coin_ATM_Pay :
				return stateVector[1] == State.coin_ATM_Pay;
			case coin_ATM_Withdraw :
				return stateVector[1] == State.coin_ATM_Withdraw;
			default :
				return false;
		}
	}

	/**
	* Set the {@link ITimer} for the state machine. It must be set
	* externally on a timed state machine before a run cycle can be correct
	* executed.
	* 
	* @param timer
	*/
	public void setTimer(ITimer timer) {
		this.timer = timer;
	}

	/**
	* Returns the currently used timer.
	* 
	* @return {@link ITimer}
	*/
	public ITimer getTimer() {
		return timer;
	}

	public void timeElapsed(int eventID) {
		timeEvents[eventID] = true;
	}

	public SCInterface getSCInterface() {
		return sCInterface;
	}

	public void raiseInsertPiece() {
		sCInterface.raiseInsertPiece();
	}
	public void raiseAddItem() {
		sCInterface.raiseAddItem();
	}
	public void raiseRefound() {
		sCInterface.raiseRefound();
	}
	public void raiseMaintenance() {
		sCInterface.raiseMaintenance();
	}
	public void raiseAdd() {
		sCInterface.raiseAdd();
	}
	public void raiseDelete() {
		sCInterface.raiseDelete();
	}
	public void raiseLogin() {
		sCInterface.raiseLogin();
	}
	public void raiseCreate() {
		sCInterface.raiseCreate();
	}
	public void raiseLoad() {
		sCInterface.raiseLoad();
	}
	public void raiseAlter() {
		sCInterface.raiseAlter();
	}
	public void raiseSave() {
		sCInterface.raiseSave();
	}
	public void raiseLogout() {
		sCInterface.raiseLogout();
	}

	public long getPiece() {
		return sCInterface.getPiece();
	}

	public void setPiece(long value) {
		sCInterface.setPiece(value);
	}
	public long getItemPrice() {
		return sCInterface.getItemPrice();
	}

	public void setItemPrice(long value) {
		sCInterface.setItemPrice(value);
	}
	public long getLoginType() {
		return sCInterface.getLoginType();
	}

	public void setLoginType(long value) {
		sCInterface.setLoginType(value);
	}
	public long getTotalPaid() {
		return sCInterface.getTotalPaid();
	}

	public void setTotalPaid(long value) {
		sCInterface.setTotalPaid(value);
	}

	private boolean check_Machine_Maintenance_tr0_tr0() {
		return sCInterface.maintenance;
	}

	private boolean check_Machine_Maintenance_r1_Authentification_tr0_tr0() {
		return sCInterface.login;
	}

	private boolean check_Machine_Maintenance_r1_Stock_tr0_tr0() {
		return sCInterface.add;
	}

	private boolean check_Machine_Maintenance_r1_Stock_tr1_tr1() {
		return sCInterface.delete;
	}

	private boolean check_Machine_Maintenance_r1_Stock_tr2_tr2() {
		return sCInterface.alter;
	}

	private boolean check_Machine_Maintenance_r1_Stock_tr3_tr3() {
		return sCInterface.logout;
	}

	private boolean check_Machine_Maintenance_r1_MachineManagement_tr0_tr0() {
		return sCInterface.create;
	}

	private boolean check_Machine_Maintenance_r1_MachineManagement_tr1_tr1() {
		return sCInterface.load;
	}

	private boolean check_Machine_Maintenance_r1_MachineManagement_tr2_tr2() {
		return sCInterface.save;
	}

	private boolean check_Machine_Maintenance_r1_MachineManagement_tr3_tr3() {
		return sCInterface.logout;
	}

	private boolean check_Machine_Maintenance_r1_CreateMachine_tr0_tr0() {
		return sCInterface.add;
	}

	private boolean check_Machine_Maintenance_r1_moduleManagement_tr0_tr0() {
		return sCInterface.add;
	}

	private boolean check_Machine_Maintenance_r1_moduleManagement_tr1_tr1() {
		return sCInterface.delete;
	}

	private boolean check_Machine_Maintenance_r1_moduleManagement_tr2_tr2() {
		return sCInterface.save;
	}

	private boolean check_Machine_Maintenance_r1_ArticleManagement_tr0_tr0() {
		return sCInterface.delete;
	}

	private boolean check_Machine_Maintenance_r1_ArticleManagement_tr1_tr1() {
		return sCInterface.add;
	}

	private boolean check_Machine_Maintenance_r1_ArticleManagement_tr2_tr2() {
		return sCInterface.save;
	}

	private boolean check_Machine_Machine_tr0_tr0() {
		return sCInterface.maintenance;
	}

	private boolean check_Machine_Machine_r1_Standby_tr0_tr0() {
		return sCInterface.addItem;
	}

	private boolean check_Machine_Machine_r1_Selection_tr0_tr0() {
		return sCInterface.getTotalPaid() >= getNeedMoney();
	}

	private boolean check_Machine_Machine_r1_Selection_tr1_tr1() {
		return timeEvents[0];
	}

	private boolean check_Machine_Machine_r1_Distribute_tr0_tr0() {
		return timeEvents[1];
	}

	private boolean check_Coin_ATM_Standby_tr0_tr0() {
		return sCInterface.insertPiece;
	}

	private boolean check_Coin_ATM_Pay_tr0_tr0() {
		return sCInterface.insertPiece;
	}

	private boolean check_Coin_ATM_Pay_tr1_tr1() {
		return sCInterface.refound;
	}

	private boolean check_Coin_ATM_Pay_tr2_tr2() {
		return isStateActive(State.machine_Maintenance);
	}

	private boolean check_Coin_ATM_Withdraw_tr0_tr0() {
		return sCInterface.getTotalPaid() == 0;
	}

	private boolean check_Machine_Maintenance_r1__choice_0_tr0_tr0() {
		return sCInterface.getLoginType() == 0;
	}

	private boolean check_Machine_Maintenance_r1__choice_0_tr1_tr1() {
		return sCInterface.getLoginType() == 1;
	}

	private boolean check_Machine_Maintenance_r1__choice_0_tr2() {
		return true;
	}

	private void effect_Machine_Maintenance_tr0() {
		exitSequence_Machine_Maintenance();

		enterSequence_Machine_Machine_default();
	}

	private void effect_Machine_Maintenance_r1_Authentification_tr0() {
		exitSequence_Machine_Maintenance_r1_Authentification();

		react_Machine_Maintenance_r1__choice_0();
	}

	private void effect_Machine_Maintenance_r1_Stock_tr0() {
		exitSequence_Machine_Maintenance_r1_Stock();

		enterSequence_Machine_Maintenance_r1_Stock_default();
	}

	private void effect_Machine_Maintenance_r1_Stock_tr1() {
		exitSequence_Machine_Maintenance_r1_Stock();

		enterSequence_Machine_Maintenance_r1_Stock_default();
	}

	private void effect_Machine_Maintenance_r1_Stock_tr2() {
		exitSequence_Machine_Maintenance_r1_Stock();

		enterSequence_Machine_Maintenance_r1_ArticleManagement_default();
	}

	private void effect_Machine_Maintenance_r1_Stock_tr3() {
		exitSequence_Machine_Maintenance_r1_Stock();

		enterSequence_Machine_Maintenance_r1_Authentification_default();
	}

	private void effect_Machine_Maintenance_r1_MachineManagement_tr0() {
		exitSequence_Machine_Maintenance_r1_MachineManagement();

		enterSequence_Machine_Maintenance_r1_CreateMachine_default();
	}

	private void effect_Machine_Maintenance_r1_MachineManagement_tr1() {
		exitSequence_Machine_Maintenance_r1_MachineManagement();

		enterSequence_Machine_Maintenance_r1_moduleManagement_default();
	}

	private void effect_Machine_Maintenance_r1_MachineManagement_tr2() {
		exitSequence_Machine_Maintenance_r1_MachineManagement();

		enterSequence_Machine_Maintenance_r1_MachineManagement_default();
	}

	private void effect_Machine_Maintenance_r1_MachineManagement_tr3() {
		exitSequence_Machine_Maintenance_r1_MachineManagement();

		enterSequence_Machine_Maintenance_r1_Authentification_default();
	}

	private void effect_Machine_Maintenance_r1_CreateMachine_tr0() {
		exitSequence_Machine_Maintenance_r1_CreateMachine();

		enterSequence_Machine_Maintenance_r1_moduleManagement_default();
	}

	private void effect_Machine_Maintenance_r1_moduleManagement_tr0() {
		exitSequence_Machine_Maintenance_r1_moduleManagement();

		enterSequence_Machine_Maintenance_r1_moduleManagement_default();
	}

	private void effect_Machine_Maintenance_r1_moduleManagement_tr1() {
		exitSequence_Machine_Maintenance_r1_moduleManagement();

		enterSequence_Machine_Maintenance_r1_moduleManagement_default();
	}

	private void effect_Machine_Maintenance_r1_moduleManagement_tr2() {
		exitSequence_Machine_Maintenance_r1_moduleManagement();

		enterSequence_Machine_Maintenance_r1_MachineManagement_default();
	}

	private void effect_Machine_Maintenance_r1_ArticleManagement_tr0() {
		exitSequence_Machine_Maintenance_r1_ArticleManagement();

		enterSequence_Machine_Maintenance_r1_ArticleManagement_default();
	}

	private void effect_Machine_Maintenance_r1_ArticleManagement_tr1() {
		exitSequence_Machine_Maintenance_r1_ArticleManagement();

		enterSequence_Machine_Maintenance_r1_ArticleManagement_default();
	}

	private void effect_Machine_Maintenance_r1_ArticleManagement_tr2() {
		exitSequence_Machine_Maintenance_r1_ArticleManagement();

		enterSequence_Machine_Maintenance_r1_Stock_default();
	}

	private void effect_Machine_Machine_tr0() {
		exitSequence_Machine_Machine();

		sCInterface.raiseRefound();

		enterSequence_Machine_Maintenance_default();
	}

	private void effect_Machine_Machine_r1_Standby_tr0() {
		exitSequence_Machine_Machine_r1_Standby();

		enterSequence_Machine_Machine_r1_Selection_default();
	}

	private void effect_Machine_Machine_r1_Selection_tr0() {
		exitSequence_Machine_Machine_r1_Selection();

		enterSequence_Machine_Machine_r1_Distribute_default();
	}

	private void effect_Machine_Machine_r1_Selection_tr1() {
		exitSequence_Machine_Machine_r1_Selection();

		enterSequence_Machine_Machine_r1_Standby_default();
	}

	private void effect_Machine_Machine_r1_Distribute_tr0() {
		exitSequence_Machine_Machine_r1_Distribute();

		enterSequence_Machine_Machine_r1_Standby_default();
	}

	private void effect_Coin_ATM_Standby_tr0() {
		exitSequence_Coin_ATM_Standby();

		enterSequence_Coin_ATM_Pay_default();
	}

	private void effect_Coin_ATM_Pay_tr0() {
		exitSequence_Coin_ATM_Pay();

		enterSequence_Coin_ATM_Pay_default();
	}

	private void effect_Coin_ATM_Pay_tr1() {
		exitSequence_Coin_ATM_Pay();

		enterSequence_Coin_ATM_Withdraw_default();
	}

	private void effect_Coin_ATM_Pay_tr2() {
		exitSequence_Coin_ATM_Pay();

		enterSequence_Coin_ATM_Withdraw_default();
	}

	private void effect_Coin_ATM_Withdraw_tr0() {
		exitSequence_Coin_ATM_Withdraw();

		enterSequence_Coin_ATM_Standby_default();
	}

	private void effect_Machine_Maintenance_r1__choice_0_tr0() {
		enterSequence_Machine_Maintenance_r1_Stock_default();
	}

	private void effect_Machine_Maintenance_r1__choice_0_tr1() {
		enterSequence_Machine_Maintenance_r1_MachineManagement_default();
	}

	private void effect_Machine_Maintenance_r1__choice_0_tr2() {
		enterSequence_Machine_Maintenance_r1_Authentification_default();
	}

	/* Entry action for state 'Standby'. */
	private void entryAction_Machine_Machine_r1_Standby() {
		setNeedMoney(0);
	}

	/* Entry action for state 'Selection'. */
	private void entryAction_Machine_Machine_r1_Selection() {

		timer.setTimer(this, 0, 10 * 1000, false);

		setNeedMoney(getNeedMoney() + sCInterface.itemPrice);
	}

	/* Entry action for state 'Distribute'. */
	private void entryAction_Machine_Machine_r1_Distribute() {

		timer.setTimer(this, 1, 1 * 1000, false);

		sCInterface.setTotalPaid(sCInterface.getTotalPaid() - needMoney);
	}

	/* Entry action for state 'Pay'. */
	private void entryAction_Coin_ATM_Pay() {
		setTotalMoney(getTotalMoney() + sCInterface.piece);

		sCInterface.setTotalPaid(sCInterface.getTotalPaid() + sCInterface.piece);
	}

	/* Entry action for state 'Withdraw'. */
	private void entryAction_Coin_ATM_Withdraw() {
		setTotalMoney(getTotalMoney() - sCInterface.totalPaid);

		sCInterface.setTotalPaid(0);
	}

	/* Exit action for state 'Selection'. */
	private void exitAction_Machine_Machine_r1_Selection() {
		timer.unsetTimer(this, 0);
	}

	/* Exit action for state 'Distribute'. */
	private void exitAction_Machine_Machine_r1_Distribute() {
		timer.unsetTimer(this, 1);

		sCInterface.raiseRefound();
	}

	/* 'default' enter sequence for state Maintenance */
	private void enterSequence_Machine_Maintenance_default() {
		enterSequence_Machine_Maintenance_r1_default();
	}

	/* 'default' enter sequence for state Authentification */
	private void enterSequence_Machine_Maintenance_r1_Authentification_default() {
		nextStateIndex = 0;
		stateVector[0] = State.machine_Maintenance_r1_Authentification;
	}

	/* 'default' enter sequence for state Stock */
	private void enterSequence_Machine_Maintenance_r1_Stock_default() {
		nextStateIndex = 0;
		stateVector[0] = State.machine_Maintenance_r1_Stock;
	}

	/* 'default' enter sequence for state MachineManagement */
	private void enterSequence_Machine_Maintenance_r1_MachineManagement_default() {
		nextStateIndex = 0;
		stateVector[0] = State.machine_Maintenance_r1_MachineManagement;
	}

	/* 'default' enter sequence for state CreateMachine */
	private void enterSequence_Machine_Maintenance_r1_CreateMachine_default() {
		nextStateIndex = 0;
		stateVector[0] = State.machine_Maintenance_r1_CreateMachine;
	}

	/* 'default' enter sequence for state moduleManagement */
	private void enterSequence_Machine_Maintenance_r1_moduleManagement_default() {
		nextStateIndex = 0;
		stateVector[0] = State.machine_Maintenance_r1_moduleManagement;
	}

	/* 'default' enter sequence for state ArticleManagement */
	private void enterSequence_Machine_Maintenance_r1_ArticleManagement_default() {
		nextStateIndex = 0;
		stateVector[0] = State.machine_Maintenance_r1_ArticleManagement;
	}

	/* 'default' enter sequence for state Machine */
	private void enterSequence_Machine_Machine_default() {
		enterSequence_Machine_Machine_r1_default();
	}

	/* 'default' enter sequence for state Standby */
	private void enterSequence_Machine_Machine_r1_Standby_default() {
		entryAction_Machine_Machine_r1_Standby();

		nextStateIndex = 0;
		stateVector[0] = State.machine_Machine_r1_Standby;
	}

	/* 'default' enter sequence for state Selection */
	private void enterSequence_Machine_Machine_r1_Selection_default() {
		entryAction_Machine_Machine_r1_Selection();

		nextStateIndex = 0;
		stateVector[0] = State.machine_Machine_r1_Selection;
	}

	/* 'default' enter sequence for state Distribute */
	private void enterSequence_Machine_Machine_r1_Distribute_default() {
		entryAction_Machine_Machine_r1_Distribute();

		nextStateIndex = 0;
		stateVector[0] = State.machine_Machine_r1_Distribute;
	}

	/* 'default' enter sequence for state Standby */
	private void enterSequence_Coin_ATM_Standby_default() {
		nextStateIndex = 1;
		stateVector[1] = State.coin_ATM_Standby;
	}

	/* 'default' enter sequence for state Pay */
	private void enterSequence_Coin_ATM_Pay_default() {
		entryAction_Coin_ATM_Pay();

		nextStateIndex = 1;
		stateVector[1] = State.coin_ATM_Pay;
	}

	/* 'default' enter sequence for state Withdraw */
	private void enterSequence_Coin_ATM_Withdraw_default() {
		entryAction_Coin_ATM_Withdraw();

		nextStateIndex = 1;
		stateVector[1] = State.coin_ATM_Withdraw;
	}

	/* 'default' enter sequence for region Machine */
	private void enterSequence_Machine_default() {
		react_Machine__entry_Default();
	}

	/* 'default' enter sequence for region r1 */
	private void enterSequence_Machine_Maintenance_r1_default() {
		react_Machine_Maintenance_r1__entry_Default();
	}

	/* 'default' enter sequence for region r1 */
	private void enterSequence_Machine_Machine_r1_default() {
		react_Machine_Machine_r1__entry_Default();
	}

	/* 'default' enter sequence for region Coin-ATM */
	private void enterSequence_Coin_ATM_default() {
		react_Coin_ATM__entry_Default();
	}

	/* Default exit sequence for state Maintenance */
	private void exitSequence_Machine_Maintenance() {
		exitSequence_Machine_Maintenance_r1();
	}

	/* Default exit sequence for state Authentification */
	private void exitSequence_Machine_Maintenance_r1_Authentification() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state Stock */
	private void exitSequence_Machine_Maintenance_r1_Stock() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state MachineManagement */
	private void exitSequence_Machine_Maintenance_r1_MachineManagement() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state CreateMachine */
	private void exitSequence_Machine_Maintenance_r1_CreateMachine() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state moduleManagement */
	private void exitSequence_Machine_Maintenance_r1_moduleManagement() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state ArticleManagement */
	private void exitSequence_Machine_Maintenance_r1_ArticleManagement() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state Machine */
	private void exitSequence_Machine_Machine() {
		exitSequence_Machine_Machine_r1();
	}

	/* Default exit sequence for state Standby */
	private void exitSequence_Machine_Machine_r1_Standby() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state Selection */
	private void exitSequence_Machine_Machine_r1_Selection() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;

		exitAction_Machine_Machine_r1_Selection();
	}

	/* Default exit sequence for state Distribute */
	private void exitSequence_Machine_Machine_r1_Distribute() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;

		exitAction_Machine_Machine_r1_Distribute();
	}

	/* Default exit sequence for state Standby */
	private void exitSequence_Coin_ATM_Standby() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}

	/* Default exit sequence for state Pay */
	private void exitSequence_Coin_ATM_Pay() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}

	/* Default exit sequence for state Withdraw */
	private void exitSequence_Coin_ATM_Withdraw() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}

	/* Default exit sequence for region Machine */
	private void exitSequence_Machine() {
		switch (stateVector[0]) {
			case machine_Maintenance_r1_Authentification :
				exitSequence_Machine_Maintenance_r1_Authentification();
				break;

			case machine_Maintenance_r1_Stock :
				exitSequence_Machine_Maintenance_r1_Stock();
				break;

			case machine_Maintenance_r1_MachineManagement :
				exitSequence_Machine_Maintenance_r1_MachineManagement();
				break;

			case machine_Maintenance_r1_CreateMachine :
				exitSequence_Machine_Maintenance_r1_CreateMachine();
				break;

			case machine_Maintenance_r1_moduleManagement :
				exitSequence_Machine_Maintenance_r1_moduleManagement();
				break;

			case machine_Maintenance_r1_ArticleManagement :
				exitSequence_Machine_Maintenance_r1_ArticleManagement();
				break;

			case machine_Machine_r1_Standby :
				exitSequence_Machine_Machine_r1_Standby();
				break;

			case machine_Machine_r1_Selection :
				exitSequence_Machine_Machine_r1_Selection();
				break;

			case machine_Machine_r1_Distribute :
				exitSequence_Machine_Machine_r1_Distribute();
				break;

			default :
				break;
		}
	}

	/* Default exit sequence for region r1 */
	private void exitSequence_Machine_Maintenance_r1() {
		switch (stateVector[0]) {
			case machine_Maintenance_r1_Authentification :
				exitSequence_Machine_Maintenance_r1_Authentification();
				break;

			case machine_Maintenance_r1_Stock :
				exitSequence_Machine_Maintenance_r1_Stock();
				break;

			case machine_Maintenance_r1_MachineManagement :
				exitSequence_Machine_Maintenance_r1_MachineManagement();
				break;

			case machine_Maintenance_r1_CreateMachine :
				exitSequence_Machine_Maintenance_r1_CreateMachine();
				break;

			case machine_Maintenance_r1_moduleManagement :
				exitSequence_Machine_Maintenance_r1_moduleManagement();
				break;

			case machine_Maintenance_r1_ArticleManagement :
				exitSequence_Machine_Maintenance_r1_ArticleManagement();
				break;

			default :
				break;
		}
	}

	/* Default exit sequence for region r1 */
	private void exitSequence_Machine_Machine_r1() {
		switch (stateVector[0]) {
			case machine_Machine_r1_Standby :
				exitSequence_Machine_Machine_r1_Standby();
				break;

			case machine_Machine_r1_Selection :
				exitSequence_Machine_Machine_r1_Selection();
				break;

			case machine_Machine_r1_Distribute :
				exitSequence_Machine_Machine_r1_Distribute();
				break;

			default :
				break;
		}
	}

	/* Default exit sequence for region Coin-ATM */
	private void exitSequence_Coin_ATM() {
		switch (stateVector[1]) {
			case coin_ATM_Standby :
				exitSequence_Coin_ATM_Standby();
				break;

			case coin_ATM_Pay :
				exitSequence_Coin_ATM_Pay();
				break;

			case coin_ATM_Withdraw :
				exitSequence_Coin_ATM_Withdraw();
				break;

			default :
				break;
		}
	}

	/* The reactions of state Authentification. */
	private void react_Machine_Maintenance_r1_Authentification() {
		if (check_Machine_Maintenance_tr0_tr0()) {
			effect_Machine_Maintenance_tr0();
		} else {
			if (check_Machine_Maintenance_r1_Authentification_tr0_tr0()) {
				effect_Machine_Maintenance_r1_Authentification_tr0();
			}
		}
	}

	/* The reactions of state Stock. */
	private void react_Machine_Maintenance_r1_Stock() {
		if (check_Machine_Maintenance_tr0_tr0()) {
			effect_Machine_Maintenance_tr0();
		} else {
			if (check_Machine_Maintenance_r1_Stock_tr0_tr0()) {
				effect_Machine_Maintenance_r1_Stock_tr0();
			} else {
				if (check_Machine_Maintenance_r1_Stock_tr1_tr1()) {
					effect_Machine_Maintenance_r1_Stock_tr1();
				} else {
					if (check_Machine_Maintenance_r1_Stock_tr2_tr2()) {
						effect_Machine_Maintenance_r1_Stock_tr2();
					} else {
						if (check_Machine_Maintenance_r1_Stock_tr3_tr3()) {
							effect_Machine_Maintenance_r1_Stock_tr3();
						}
					}
				}
			}
		}
	}

	/* The reactions of state MachineManagement. */
	private void react_Machine_Maintenance_r1_MachineManagement() {
		if (check_Machine_Maintenance_tr0_tr0()) {
			effect_Machine_Maintenance_tr0();
		} else {
			if (check_Machine_Maintenance_r1_MachineManagement_tr0_tr0()) {
				effect_Machine_Maintenance_r1_MachineManagement_tr0();
			} else {
				if (check_Machine_Maintenance_r1_MachineManagement_tr1_tr1()) {
					effect_Machine_Maintenance_r1_MachineManagement_tr1();
				} else {
					if (check_Machine_Maintenance_r1_MachineManagement_tr2_tr2()) {
						effect_Machine_Maintenance_r1_MachineManagement_tr2();
					} else {
						if (check_Machine_Maintenance_r1_MachineManagement_tr3_tr3()) {
							effect_Machine_Maintenance_r1_MachineManagement_tr3();
						}
					}
				}
			}
		}
	}

	/* The reactions of state CreateMachine. */
	private void react_Machine_Maintenance_r1_CreateMachine() {
		if (check_Machine_Maintenance_tr0_tr0()) {
			effect_Machine_Maintenance_tr0();
		} else {
			if (check_Machine_Maintenance_r1_CreateMachine_tr0_tr0()) {
				effect_Machine_Maintenance_r1_CreateMachine_tr0();
			}
		}
	}

	/* The reactions of state moduleManagement. */
	private void react_Machine_Maintenance_r1_moduleManagement() {
		if (check_Machine_Maintenance_tr0_tr0()) {
			effect_Machine_Maintenance_tr0();
		} else {
			if (check_Machine_Maintenance_r1_moduleManagement_tr0_tr0()) {
				effect_Machine_Maintenance_r1_moduleManagement_tr0();
			} else {
				if (check_Machine_Maintenance_r1_moduleManagement_tr1_tr1()) {
					effect_Machine_Maintenance_r1_moduleManagement_tr1();
				} else {
					if (check_Machine_Maintenance_r1_moduleManagement_tr2_tr2()) {
						effect_Machine_Maintenance_r1_moduleManagement_tr2();
					}
				}
			}
		}
	}

	/* The reactions of state ArticleManagement. */
	private void react_Machine_Maintenance_r1_ArticleManagement() {
		if (check_Machine_Maintenance_tr0_tr0()) {
			effect_Machine_Maintenance_tr0();
		} else {
			if (check_Machine_Maintenance_r1_ArticleManagement_tr0_tr0()) {
				effect_Machine_Maintenance_r1_ArticleManagement_tr0();
			} else {
				if (check_Machine_Maintenance_r1_ArticleManagement_tr1_tr1()) {
					effect_Machine_Maintenance_r1_ArticleManagement_tr1();
				} else {
					if (check_Machine_Maintenance_r1_ArticleManagement_tr2_tr2()) {
						effect_Machine_Maintenance_r1_ArticleManagement_tr2();
					}
				}
			}
		}
	}

	/* The reactions of state Standby. */
	private void react_Machine_Machine_r1_Standby() {
		if (check_Machine_Machine_tr0_tr0()) {
			effect_Machine_Machine_tr0();
		} else {
			if (check_Machine_Machine_r1_Standby_tr0_tr0()) {
				effect_Machine_Machine_r1_Standby_tr0();
			}
		}
	}

	/* The reactions of state Selection. */
	private void react_Machine_Machine_r1_Selection() {
		if (check_Machine_Machine_tr0_tr0()) {
			effect_Machine_Machine_tr0();
		} else {
			if (check_Machine_Machine_r1_Selection_tr0_tr0()) {
				effect_Machine_Machine_r1_Selection_tr0();
			} else {
				if (check_Machine_Machine_r1_Selection_tr1_tr1()) {
					effect_Machine_Machine_r1_Selection_tr1();
				}
			}
		}
	}

	/* The reactions of state Distribute. */
	private void react_Machine_Machine_r1_Distribute() {
		if (check_Machine_Machine_tr0_tr0()) {
			effect_Machine_Machine_tr0();
		} else {
			if (check_Machine_Machine_r1_Distribute_tr0_tr0()) {
				effect_Machine_Machine_r1_Distribute_tr0();
			}
		}
	}

	/* The reactions of state Standby. */
	private void react_Coin_ATM_Standby() {
		if (check_Coin_ATM_Standby_tr0_tr0()) {
			effect_Coin_ATM_Standby_tr0();
		}
	}

	/* The reactions of state Pay. */
	private void react_Coin_ATM_Pay() {
		if (check_Coin_ATM_Pay_tr0_tr0()) {
			effect_Coin_ATM_Pay_tr0();
		} else {
			if (check_Coin_ATM_Pay_tr1_tr1()) {
				effect_Coin_ATM_Pay_tr1();
			} else {
				if (check_Coin_ATM_Pay_tr2_tr2()) {
					effect_Coin_ATM_Pay_tr2();
				}
			}
		}
	}

	/* The reactions of state Withdraw. */
	private void react_Coin_ATM_Withdraw() {
		if (check_Coin_ATM_Withdraw_tr0_tr0()) {
			effect_Coin_ATM_Withdraw_tr0();
		}
	}

	/* The reactions of state null. */
	private void react_Machine_Maintenance_r1__choice_0() {
		if (check_Machine_Maintenance_r1__choice_0_tr0_tr0()) {
			effect_Machine_Maintenance_r1__choice_0_tr0();
		} else {
			if (check_Machine_Maintenance_r1__choice_0_tr1_tr1()) {
				effect_Machine_Maintenance_r1__choice_0_tr1();
			} else {
				effect_Machine_Maintenance_r1__choice_0_tr2();
			}
		}
	}

	/* Default react sequence for initial entry  */
	private void react_Machine_Maintenance_r1__entry_Default() {
		enterSequence_Machine_Maintenance_r1_Authentification_default();
	}

	/* Default react sequence for initial entry  */
	private void react_Machine_Machine_r1__entry_Default() {
		enterSequence_Machine_Machine_r1_Standby_default();
	}

	/* Default react sequence for initial entry  */
	private void react_Machine__entry_Default() {
		enterSequence_Machine_Maintenance_default();
	}

	/* Default react sequence for initial entry  */
	private void react_Coin_ATM__entry_Default() {
		enterSequence_Coin_ATM_Standby_default();
	}

	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The statemachine needs to be initialized first by calling the init() function.");

		clearOutEvents();

		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {

			switch (stateVector[nextStateIndex]) {
				case machine_Maintenance_r1_Authentification :
					react_Machine_Maintenance_r1_Authentification();
					break;
				case machine_Maintenance_r1_Stock :
					react_Machine_Maintenance_r1_Stock();
					break;
				case machine_Maintenance_r1_MachineManagement :
					react_Machine_Maintenance_r1_MachineManagement();
					break;
				case machine_Maintenance_r1_CreateMachine :
					react_Machine_Maintenance_r1_CreateMachine();
					break;
				case machine_Maintenance_r1_moduleManagement :
					react_Machine_Maintenance_r1_moduleManagement();
					break;
				case machine_Maintenance_r1_ArticleManagement :
					react_Machine_Maintenance_r1_ArticleManagement();
					break;
				case machine_Machine_r1_Standby :
					react_Machine_Machine_r1_Standby();
					break;
				case machine_Machine_r1_Selection :
					react_Machine_Machine_r1_Selection();
					break;
				case machine_Machine_r1_Distribute :
					react_Machine_Machine_r1_Distribute();
					break;
				case coin_ATM_Standby :
					react_Coin_ATM_Standby();
					break;
				case coin_ATM_Pay :
					react_Coin_ATM_Pay();
					break;
				case coin_ATM_Withdraw :
					react_Coin_ATM_Withdraw();
					break;
				default :
					// $NullState$
			}
		}

		clearEvents();
	}
}
