package org.yakindu.scr.defaultsm;
import org.yakindu.scr.ITimer;

public class DefaultSMStatemachine implements IDefaultSMStatemachine {

	private final boolean[] timeEvents = new boolean[2];

	private final class SCInterfaceImpl implements SCInterface {

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

		private double piece;
		public double getPiece() {
			return piece;
		}

		public void setPiece(double value) {
			this.piece = value;
		}

		private double itemPrice;
		public double getItemPrice() {
			return itemPrice;
		}

		public void setItemPrice(double value) {
			this.itemPrice = value;
		}

		private long loginType;
		public long getLoginType() {
			return loginType;
		}

		public void setLoginType(long value) {
			this.loginType = value;
		}

		public void clearEvents() {
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
		}

	}

	private SCInterfaceImpl sCInterface;

	public enum State {
		machine_Maintenance, machine_Maintenance_r1_Authentification, machine_Maintenance_r1_Stock, machine_Maintenance_r1_MachineManagement, machine_Maintenance_r1_CreateMachine, machine_Maintenance_r1_moduleManagement, machine_Maintenance_r1_ArticleManagement, machine_Machine, machine_Machine_r1_Standby, machine_Machine_r1_Selection, machine_Machine_r1_Distribute, coin_ATM_Standby, coin_ATM_Pay, coin_ATM_Withdraw, $NullState$
	};

	private double totalMoney;
	private double totalPaid;
	private double needMoney;
	private boolean enabled;

	private final State[] stateVector = new State[2];

	private int nextStateIndex;

	private ITimer timer;

	static {
	}

	public DefaultSMStatemachine() {

		sCInterface = new SCInterfaceImpl();
	}

	public void init() {
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		for (int i = 0; i < 2; i++) {
			stateVector[i] = State.$NullState$;
		}

		clearEvents();
		clearOutEvents();

		sCInterface.piece = 0.5;

		sCInterface.itemPrice = 1;

		sCInterface.loginType = 0;

		totalMoney = 0;

		totalPaid = 0;

		needMoney = 0;

		enabled = false;
	}

	public void enter() {
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		entryAction();

		nextStateIndex = 0;
		stateVector[0] = State.machine_Maintenance_r1_Authentification;

		nextStateIndex = 1;
		stateVector[1] = State.coin_ATM_Standby;
	}

	public void exit() {
		switch (stateVector[0]) {
			case machine_Maintenance_r1_Authentification :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;
				break;

			case machine_Maintenance_r1_Stock :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;
				break;

			case machine_Maintenance_r1_MachineManagement :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;
				break;

			case machine_Maintenance_r1_CreateMachine :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;
				break;

			case machine_Maintenance_r1_moduleManagement :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;
				break;

			case machine_Maintenance_r1_ArticleManagement :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;
				break;

			case machine_Machine_r1_Standby :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;
				break;

			case machine_Machine_r1_Selection :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				timer.unsetTimer(this, 0);
				break;

			case machine_Machine_r1_Distribute :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				timer.unsetTimer(this, 1);

				sCInterface.raiseRefound();
				break;

			default :
				break;
		}

		switch (stateVector[1]) {
			case coin_ATM_Standby :
				nextStateIndex = 1;
				stateVector[1] = State.$NullState$;
				break;

			case coin_ATM_Pay :
				nextStateIndex = 1;
				stateVector[1] = State.$NullState$;
				break;

			case coin_ATM_Withdraw :
				nextStateIndex = 1;
				stateVector[1] = State.$NullState$;
				break;

			default :
				break;
		}

		exitAction();
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
				return stateVector[0].ordinal() >= State.machine_Maintenance
						.ordinal()
						&& stateVector[0].ordinal() <= State.machine_Maintenance_r1_ArticleManagement
								.ordinal();
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
				return stateVector[0].ordinal() >= State.machine_Machine
						.ordinal()
						&& stateVector[0].ordinal() <= State.machine_Machine_r1_Distribute
								.ordinal();
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

	public double getPiece() {
		return sCInterface.getPiece();
	}

	public void setPiece(double value) {
		sCInterface.setPiece(value);
	}
	public double getItemPrice() {
		return sCInterface.getItemPrice();
	}

	public void setItemPrice(double value) {
		sCInterface.setItemPrice(value);
	}
	public long getLoginType() {
		return sCInterface.getLoginType();
	}

	public void setLoginType(long value) {
		sCInterface.setLoginType(value);
	}

	/* Entry action for statechart 'default'. */
	private void entryAction() {
	}

	/* Exit action for state 'default'. */
	private void exitAction() {
	}

	/* The reactions of state Authentification. */
	private void reactMachine_Maintenance_r1_Authentification() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case machine_Maintenance_r1_Authentification :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_Stock :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_MachineManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_CreateMachine :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_moduleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_ArticleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				default :
					break;
			}

			needMoney = 0;

			nextStateIndex = 0;
			stateVector[0] = State.machine_Machine_r1_Standby;
		} else {
			if (sCInterface.login) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				if (sCInterface.loginType == 0) {
					nextStateIndex = 0;
					stateVector[0] = State.machine_Maintenance_r1_Stock;
				} else {
					if (sCInterface.loginType == 1) {
						nextStateIndex = 0;
						stateVector[0] = State.machine_Maintenance_r1_MachineManagement;
					} else {
						nextStateIndex = 0;
						stateVector[0] = State.machine_Maintenance_r1_Authentification;
					}
				}
			}
		}
	}

	/* The reactions of state Stock. */
	private void reactMachine_Maintenance_r1_Stock() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case machine_Maintenance_r1_Authentification :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_Stock :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_MachineManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_CreateMachine :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_moduleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_ArticleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				default :
					break;
			}

			needMoney = 0;

			nextStateIndex = 0;
			stateVector[0] = State.machine_Machine_r1_Standby;
		} else {
			if (sCInterface.add) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				nextStateIndex = 0;
				stateVector[0] = State.machine_Maintenance_r1_Stock;
			} else {
				if (sCInterface.delete) {
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					nextStateIndex = 0;
					stateVector[0] = State.machine_Maintenance_r1_Stock;
				} else {
					if (sCInterface.alter) {
						nextStateIndex = 0;
						stateVector[0] = State.$NullState$;

						nextStateIndex = 0;
						stateVector[0] = State.machine_Maintenance_r1_ArticleManagement;
					}
				}
			}
		}
	}

	/* The reactions of state MachineManagement. */
	private void reactMachine_Maintenance_r1_MachineManagement() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case machine_Maintenance_r1_Authentification :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_Stock :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_MachineManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_CreateMachine :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_moduleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_ArticleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				default :
					break;
			}

			needMoney = 0;

			nextStateIndex = 0;
			stateVector[0] = State.machine_Machine_r1_Standby;
		} else {
			if (sCInterface.create) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				nextStateIndex = 0;
				stateVector[0] = State.machine_Maintenance_r1_CreateMachine;
			} else {
				if (sCInterface.load) {
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					nextStateIndex = 0;
					stateVector[0] = State.machine_Maintenance_r1_moduleManagement;
				} else {
					if (sCInterface.save) {
						nextStateIndex = 0;
						stateVector[0] = State.$NullState$;

						nextStateIndex = 0;
						stateVector[0] = State.machine_Maintenance_r1_MachineManagement;
					}
				}
			}
		}
	}

	/* The reactions of state CreateMachine. */
	private void reactMachine_Maintenance_r1_CreateMachine() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case machine_Maintenance_r1_Authentification :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_Stock :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_MachineManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_CreateMachine :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_moduleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_ArticleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				default :
					break;
			}

			needMoney = 0;

			nextStateIndex = 0;
			stateVector[0] = State.machine_Machine_r1_Standby;
		} else {
			if (sCInterface.add) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				nextStateIndex = 0;
				stateVector[0] = State.machine_Maintenance_r1_moduleManagement;
			}
		}
	}

	/* The reactions of state moduleManagement. */
	private void reactMachine_Maintenance_r1_moduleManagement() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case machine_Maintenance_r1_Authentification :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_Stock :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_MachineManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_CreateMachine :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_moduleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_ArticleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				default :
					break;
			}

			needMoney = 0;

			nextStateIndex = 0;
			stateVector[0] = State.machine_Machine_r1_Standby;
		} else {
			if (sCInterface.add) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				nextStateIndex = 0;
				stateVector[0] = State.machine_Maintenance_r1_moduleManagement;
			} else {
				if (sCInterface.delete) {
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					nextStateIndex = 0;
					stateVector[0] = State.machine_Maintenance_r1_moduleManagement;
				} else {
					if (sCInterface.save) {
						nextStateIndex = 0;
						stateVector[0] = State.$NullState$;

						nextStateIndex = 0;
						stateVector[0] = State.machine_Maintenance_r1_MachineManagement;
					}
				}
			}
		}
	}

	/* The reactions of state ArticleManagement. */
	private void reactMachine_Maintenance_r1_ArticleManagement() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case machine_Maintenance_r1_Authentification :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_Stock :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_MachineManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_CreateMachine :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_moduleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Maintenance_r1_ArticleManagement :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				default :
					break;
			}

			needMoney = 0;

			nextStateIndex = 0;
			stateVector[0] = State.machine_Machine_r1_Standby;
		} else {
			if (sCInterface.delete) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				nextStateIndex = 0;
				stateVector[0] = State.machine_Maintenance_r1_ArticleManagement;
			} else {
				if (sCInterface.add) {
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					nextStateIndex = 0;
					stateVector[0] = State.machine_Maintenance_r1_ArticleManagement;
				} else {
					if (sCInterface.save) {
						nextStateIndex = 0;
						stateVector[0] = State.$NullState$;

						nextStateIndex = 0;
						stateVector[0] = State.machine_Maintenance_r1_Stock;
					}
				}
			}
		}
	}

	/* The reactions of state Standby. */
	private void reactMachine_Machine_r1_Standby() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case machine_Machine_r1_Standby :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Machine_r1_Selection :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 0);
					break;

				case machine_Machine_r1_Distribute :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 1);

					sCInterface.raiseRefound();
					break;

				default :
					break;
			}

			sCInterface.raiseRefound();

			nextStateIndex = 0;
			stateVector[0] = State.machine_Maintenance_r1_Authentification;
		} else {
			if (sCInterface.addItem) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				timer.setTimer(this, 0, 10 * 1000, false);

				needMoney += sCInterface.itemPrice;

				nextStateIndex = 0;
				stateVector[0] = State.machine_Machine_r1_Selection;
			}
		}
	}

	/* The reactions of state Selection. */
	private void reactMachine_Machine_r1_Selection() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case machine_Machine_r1_Standby :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Machine_r1_Selection :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 0);
					break;

				case machine_Machine_r1_Distribute :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 1);

					sCInterface.raiseRefound();
					break;

				default :
					break;
			}

			sCInterface.raiseRefound();

			nextStateIndex = 0;
			stateVector[0] = State.machine_Maintenance_r1_Authentification;
		} else {
			if (totalPaid >= needMoney) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				timer.unsetTimer(this, 0);

				timer.setTimer(this, 1, 1 * 1000, false);

				totalPaid -= needMoney;

				nextStateIndex = 0;
				stateVector[0] = State.machine_Machine_r1_Distribute;
			} else {
				if (timeEvents[0]) {
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 0);

					needMoney = 0;

					nextStateIndex = 0;
					stateVector[0] = State.machine_Machine_r1_Standby;
				}
			}
		}
	}

	/* The reactions of state Distribute. */
	private void reactMachine_Machine_r1_Distribute() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case machine_Machine_r1_Standby :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				case machine_Machine_r1_Selection :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 0);
					break;

				case machine_Machine_r1_Distribute :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 1);

					sCInterface.raiseRefound();
					break;

				default :
					break;
			}

			sCInterface.raiseRefound();

			nextStateIndex = 0;
			stateVector[0] = State.machine_Maintenance_r1_Authentification;
		} else {
			if (timeEvents[1]) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				timer.unsetTimer(this, 1);

				sCInterface.raiseRefound();

				needMoney = 0;

				nextStateIndex = 0;
				stateVector[0] = State.machine_Machine_r1_Standby;
			}
		}
	}

	/* The reactions of state Standby. */
	private void reactCoin_ATM_Standby() {
		if (sCInterface.insertPiece) {
			nextStateIndex = 1;
			stateVector[1] = State.$NullState$;

			totalMoney += sCInterface.piece;

			totalPaid += sCInterface.piece;

			nextStateIndex = 1;
			stateVector[1] = State.coin_ATM_Pay;
		}
	}

	/* The reactions of state Pay. */
	private void reactCoin_ATM_Pay() {
		if (sCInterface.insertPiece) {
			nextStateIndex = 1;
			stateVector[1] = State.$NullState$;

			totalMoney += sCInterface.piece;

			totalPaid += sCInterface.piece;

			nextStateIndex = 1;
			stateVector[1] = State.coin_ATM_Pay;
		} else {
			if (sCInterface.refound) {
				nextStateIndex = 1;
				stateVector[1] = State.$NullState$;

				totalMoney -= totalPaid;

				totalPaid = 0;

				nextStateIndex = 1;
				stateVector[1] = State.coin_ATM_Withdraw;
			} else {
				if (isStateActive(State.machine_Maintenance)) {
					nextStateIndex = 1;
					stateVector[1] = State.$NullState$;

					totalMoney -= totalPaid;

					totalPaid = 0;

					nextStateIndex = 1;
					stateVector[1] = State.coin_ATM_Withdraw;
				}
			}
		}
	}

	/* The reactions of state Withdraw. */
	private void reactCoin_ATM_Withdraw() {
		if (totalPaid == 0) {
			nextStateIndex = 1;
			stateVector[1] = State.$NullState$;

			nextStateIndex = 1;
			stateVector[1] = State.coin_ATM_Standby;
		}
	}

	public void runCycle() {

		clearOutEvents();

		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {

			switch (stateVector[nextStateIndex]) {
				case machine_Maintenance_r1_Authentification :
					reactMachine_Maintenance_r1_Authentification();
					break;
				case machine_Maintenance_r1_Stock :
					reactMachine_Maintenance_r1_Stock();
					break;
				case machine_Maintenance_r1_MachineManagement :
					reactMachine_Maintenance_r1_MachineManagement();
					break;
				case machine_Maintenance_r1_CreateMachine :
					reactMachine_Maintenance_r1_CreateMachine();
					break;
				case machine_Maintenance_r1_moduleManagement :
					reactMachine_Maintenance_r1_moduleManagement();
					break;
				case machine_Maintenance_r1_ArticleManagement :
					reactMachine_Maintenance_r1_ArticleManagement();
					break;
				case machine_Machine_r1_Standby :
					reactMachine_Machine_r1_Standby();
					break;
				case machine_Machine_r1_Selection :
					reactMachine_Machine_r1_Selection();
					break;
				case machine_Machine_r1_Distribute :
					reactMachine_Machine_r1_Distribute();
					break;
				case coin_ATM_Standby :
					reactCoin_ATM_Standby();
					break;
				case coin_ATM_Pay :
					reactCoin_ATM_Pay();
					break;
				case coin_ATM_Withdraw :
					reactCoin_ATM_Withdraw();
					break;
				default :
					// $NullState$
			}
		}

		clearEvents();
	}
}
