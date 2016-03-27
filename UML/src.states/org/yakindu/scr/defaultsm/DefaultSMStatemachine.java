package org.yakindu.scr.defaultsm;
import org.yakindu.scr.ITimer;

public class DefaultSMStatemachine implements IDefaultSMStatemachine {

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

		protected void clearEvents() {
			insertPiece = false;
			addItem = false;
			refound = false;
			maintenance = false;
		}

	}

	protected SCInterfaceImpl sCInterface;

	private boolean initialized = false;

	public enum State {
		distributeur_Distributeur, distributeur_Distributeur_r1_distribution, distributeur_Distributeur_r1_Selection, distributeur_Distributeur_r1_Attente, distributeur_Maintenance, distributeur_Maintenance_r1_StandBy, monayeur_Attente, monayeur_Paiement, monayeur_rendreMonaie, $NullState$
	};

	private State[] historyVector = new State[1];
	private final State[] stateVector = new State[2];

	private int nextStateIndex;

	private ITimer timer;

	private final boolean[] timeEvents = new boolean[2];

	private double totalMoney;

	protected void setTotalMoney(double value) {
		totalMoney = value;
	}

	protected double getTotalMoney() {
		return totalMoney;
	}

	private double totalPaid;

	protected void setTotalPaid(double value) {
		totalPaid = value;
	}

	protected double getTotalPaid() {
		return totalPaid;
	}

	private double needMoney;

	protected void setNeedMoney(double value) {
		needMoney = value;
	}

	protected double getNeedMoney() {
		return needMoney;
	}

	private boolean enabled;

	protected void setEnabled(boolean value) {
		enabled = value;
	}

	protected boolean getEnabled() {
		return enabled;
	}

	public DefaultSMStatemachine() {

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

		for (int i = 0; i < 1; i++) {
			historyVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();

		sCInterface.setPiece(0.5);

		sCInterface.setItemPrice(1);

		setTotalMoney(0);

		setTotalPaid(0);

		setNeedMoney(0);

		setEnabled(false);
	}

	public void enter() {
		if (!initialized)
			throw new IllegalStateException(
					"The statemachine needs to be initialized first by calling the init() function.");

		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		enterSequence_Distributeur_default();

		enterSequence_Monayeur_default();
	}

	public void exit() {
		exitSequence_Distributeur();

		exitSequence_Monayeur();
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
			case distributeur_Distributeur :
				return stateVector[0].ordinal() >= State.distributeur_Distributeur.ordinal()
						&& stateVector[0].ordinal() <= State.distributeur_Distributeur_r1_Attente.ordinal();
			case distributeur_Distributeur_r1_distribution :
				return stateVector[0] == State.distributeur_Distributeur_r1_distribution;
			case distributeur_Distributeur_r1_Selection :
				return stateVector[0] == State.distributeur_Distributeur_r1_Selection;
			case distributeur_Distributeur_r1_Attente :
				return stateVector[0] == State.distributeur_Distributeur_r1_Attente;
			case distributeur_Maintenance :
				return stateVector[0].ordinal() >= State.distributeur_Maintenance.ordinal()
						&& stateVector[0].ordinal() <= State.distributeur_Maintenance_r1_StandBy.ordinal();
			case distributeur_Maintenance_r1_StandBy :
				return stateVector[0] == State.distributeur_Maintenance_r1_StandBy;
			case monayeur_Attente :
				return stateVector[1] == State.monayeur_Attente;
			case monayeur_Paiement :
				return stateVector[1] == State.monayeur_Paiement;
			case monayeur_rendreMonaie :
				return stateVector[1] == State.monayeur_rendreMonaie;
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

	private boolean check_Distributeur_Distributeur_tr0_tr0() {
		return sCInterface.maintenance;
	}

	private boolean check_Distributeur_Distributeur_r1_distribution_tr0_tr0() {
		return timeEvents[0];
	}

	private boolean check_Distributeur_Distributeur_r1_Selection_tr0_tr0() {
		return getTotalPaid() >= getNeedMoney();
	}

	private boolean check_Distributeur_Distributeur_r1_Selection_tr1_tr1() {
		return timeEvents[1];
	}

	private boolean check_Distributeur_Distributeur_r1_Attente_tr0_tr0() {
		return sCInterface.addItem;
	}

	private boolean check_Distributeur_Maintenance_r1_StandBy_tr0_tr0() {
		return sCInterface.maintenance;
	}

	private boolean check_Distributeur_Maintenance_r1_StandBy_tr1_tr1() {
		return getTotalPaid() > 0;
	}

	private boolean check_Monayeur_Attente_tr0_tr0() {
		return sCInterface.insertPiece;
	}

	private boolean check_Monayeur_Paiement_tr0_tr0() {
		return sCInterface.insertPiece;
	}

	private boolean check_Monayeur_Paiement_tr1_tr1() {
		return sCInterface.refound;
	}

	private boolean check_Monayeur_rendreMonaie_tr0_tr0() {
		return getTotalPaid() == 0;
	}

	private void effect_Distributeur_Distributeur_tr0() {
		exitSequence_Distributeur_Distributeur();

		enterSequence_Distributeur_Maintenance_default();
	}

	private void effect_Distributeur_Distributeur_r1_distribution_tr0() {
		exitSequence_Distributeur_Distributeur_r1_distribution();

		enterSequence_Distributeur_Distributeur_r1_Attente_default();
	}

	private void effect_Distributeur_Distributeur_r1_Selection_tr0() {
		exitSequence_Distributeur_Distributeur_r1_Selection();

		enterSequence_Distributeur_Distributeur_r1_distribution_default();
	}

	private void effect_Distributeur_Distributeur_r1_Selection_tr1() {
		exitSequence_Distributeur_Distributeur_r1_Selection();

		enterSequence_Distributeur_Distributeur_r1_Attente_default();
	}

	private void effect_Distributeur_Distributeur_r1_Attente_tr0() {
		exitSequence_Distributeur_Distributeur_r1_Attente();

		enterSequence_Distributeur_Distributeur_r1_Selection_default();
	}

	private void effect_Distributeur_Maintenance_r1_StandBy_tr0() {
		exitSequence_Distributeur_Maintenance();

		react_Distributeur_Distributeur_r1_hist();
	}

	private void effect_Distributeur_Maintenance_r1_StandBy_tr1() {
		exitSequence_Distributeur_Maintenance_r1_StandBy();

		enterSequence_Distributeur_Maintenance_r1_StandBy_default();
	}

	private void effect_Monayeur_Attente_tr0() {
		exitSequence_Monayeur_Attente();

		enterSequence_Monayeur_Paiement_default();
	}

	private void effect_Monayeur_Paiement_tr0() {
		exitSequence_Monayeur_Paiement();

		enterSequence_Monayeur_Paiement_default();
	}

	private void effect_Monayeur_Paiement_tr1() {
		exitSequence_Monayeur_Paiement();

		enterSequence_Monayeur_rendreMonaie_default();
	}

	private void effect_Monayeur_rendreMonaie_tr0() {
		exitSequence_Monayeur_rendreMonaie();

		enterSequence_Monayeur_Attente_default();
	}

	/* Entry action for state 'distribution'. */
	private void entryAction_Distributeur_Distributeur_r1_distribution() {

		timer.setTimer(this, 0, 1 * 1000, false);

		setTotalPaid(getTotalPaid() - needMoney);
	}

	/* Entry action for state 'Selection'. */
	private void entryAction_Distributeur_Distributeur_r1_Selection() {

		timer.setTimer(this, 1, 10 * 1000, false);

		setNeedMoney(getNeedMoney() + sCInterface.itemPrice);
	}

	/* Entry action for state 'Attente'. */
	private void entryAction_Distributeur_Distributeur_r1_Attente() {
		setNeedMoney(0);
	}

	/* Entry action for state 'StandBy'. */
	private void entryAction_Distributeur_Maintenance_r1_StandBy() {
		sCInterface.raiseRefound();
	}

	/* Entry action for state 'Paiement'. */
	private void entryAction_Monayeur_Paiement() {
		setTotalMoney(getTotalMoney() + sCInterface.piece);

		setTotalPaid(getTotalPaid() + sCInterface.piece);
	}

	/* Entry action for state 'rendreMonaie'. */
	private void entryAction_Monayeur_rendreMonaie() {
		setTotalMoney(getTotalMoney() - totalPaid);

		setTotalPaid(0);
	}

	/* Exit action for state 'distribution'. */
	private void exitAction_Distributeur_Distributeur_r1_distribution() {
		timer.unsetTimer(this, 0);

		sCInterface.raiseRefound();
	}

	/* Exit action for state 'Selection'. */
	private void exitAction_Distributeur_Distributeur_r1_Selection() {
		timer.unsetTimer(this, 1);
	}

	/* 'default' enter sequence for state Distributeur */
	private void enterSequence_Distributeur_Distributeur_default() {
		enterSequence_Distributeur_Distributeur_r1_default();
	}

	/* 'default' enter sequence for state distribution */
	private void enterSequence_Distributeur_Distributeur_r1_distribution_default() {
		entryAction_Distributeur_Distributeur_r1_distribution();

		nextStateIndex = 0;
		stateVector[0] = State.distributeur_Distributeur_r1_distribution;

		historyVector[0] = stateVector[0];
	}

	/* 'default' enter sequence for state Selection */
	private void enterSequence_Distributeur_Distributeur_r1_Selection_default() {
		entryAction_Distributeur_Distributeur_r1_Selection();

		nextStateIndex = 0;
		stateVector[0] = State.distributeur_Distributeur_r1_Selection;

		historyVector[0] = stateVector[0];
	}

	/* 'default' enter sequence for state Attente */
	private void enterSequence_Distributeur_Distributeur_r1_Attente_default() {
		entryAction_Distributeur_Distributeur_r1_Attente();

		nextStateIndex = 0;
		stateVector[0] = State.distributeur_Distributeur_r1_Attente;

		historyVector[0] = stateVector[0];
	}

	/* 'default' enter sequence for state Maintenance */
	private void enterSequence_Distributeur_Maintenance_default() {
		enterSequence_Distributeur_Maintenance_r1_default();
	}

	/* 'default' enter sequence for state StandBy */
	private void enterSequence_Distributeur_Maintenance_r1_StandBy_default() {
		entryAction_Distributeur_Maintenance_r1_StandBy();

		nextStateIndex = 0;
		stateVector[0] = State.distributeur_Maintenance_r1_StandBy;
	}

	/* 'default' enter sequence for state Attente */
	private void enterSequence_Monayeur_Attente_default() {
		nextStateIndex = 1;
		stateVector[1] = State.monayeur_Attente;
	}

	/* 'default' enter sequence for state Paiement */
	private void enterSequence_Monayeur_Paiement_default() {
		entryAction_Monayeur_Paiement();

		nextStateIndex = 1;
		stateVector[1] = State.monayeur_Paiement;
	}

	/* 'default' enter sequence for state rendreMonaie */
	private void enterSequence_Monayeur_rendreMonaie_default() {
		entryAction_Monayeur_rendreMonaie();

		nextStateIndex = 1;
		stateVector[1] = State.monayeur_rendreMonaie;
	}

	/* 'default' enter sequence for region Distributeur */
	private void enterSequence_Distributeur_default() {
		react_Distributeur__entry_Default();
	}

	/* 'default' enter sequence for region r1 */
	private void enterSequence_Distributeur_Distributeur_r1_default() {
		react_Distributeur_Distributeur_r1__entry_Default();
	}

	/* shallow enterSequence with history in child r1 */
	private void shallowEnterSequence_Distributeur_Distributeur_r1() {
		switch (historyVector[0]) {
			case distributeur_Distributeur_r1_distribution :
				enterSequence_Distributeur_Distributeur_r1_distribution_default();
				break;

			case distributeur_Distributeur_r1_Selection :
				enterSequence_Distributeur_Distributeur_r1_Selection_default();
				break;

			case distributeur_Distributeur_r1_Attente :
				enterSequence_Distributeur_Distributeur_r1_Attente_default();
				break;

			default :
				break;
		}
	}

	/* 'default' enter sequence for region r1 */
	private void enterSequence_Distributeur_Maintenance_r1_default() {
		react_Distributeur_Maintenance_r1__entry_Default();
	}

	/* 'default' enter sequence for region Monayeur */
	private void enterSequence_Monayeur_default() {
		react_Monayeur__entry_Default();
	}

	/* Default exit sequence for state Distributeur */
	private void exitSequence_Distributeur_Distributeur() {
		exitSequence_Distributeur_Distributeur_r1();
	}

	/* Default exit sequence for state distribution */
	private void exitSequence_Distributeur_Distributeur_r1_distribution() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;

		exitAction_Distributeur_Distributeur_r1_distribution();
	}

	/* Default exit sequence for state Selection */
	private void exitSequence_Distributeur_Distributeur_r1_Selection() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;

		exitAction_Distributeur_Distributeur_r1_Selection();
	}

	/* Default exit sequence for state Attente */
	private void exitSequence_Distributeur_Distributeur_r1_Attente() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state Maintenance */
	private void exitSequence_Distributeur_Maintenance() {
		exitSequence_Distributeur_Maintenance_r1();
	}

	/* Default exit sequence for state StandBy */
	private void exitSequence_Distributeur_Maintenance_r1_StandBy() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state Attente */
	private void exitSequence_Monayeur_Attente() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}

	/* Default exit sequence for state Paiement */
	private void exitSequence_Monayeur_Paiement() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}

	/* Default exit sequence for state rendreMonaie */
	private void exitSequence_Monayeur_rendreMonaie() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}

	/* Default exit sequence for region Distributeur */
	private void exitSequence_Distributeur() {
		switch (stateVector[0]) {
			case distributeur_Distributeur_r1_distribution :
				exitSequence_Distributeur_Distributeur_r1_distribution();
				break;

			case distributeur_Distributeur_r1_Selection :
				exitSequence_Distributeur_Distributeur_r1_Selection();
				break;

			case distributeur_Distributeur_r1_Attente :
				exitSequence_Distributeur_Distributeur_r1_Attente();
				break;

			case distributeur_Maintenance_r1_StandBy :
				exitSequence_Distributeur_Maintenance_r1_StandBy();
				break;

			default :
				break;
		}
	}

	/* Default exit sequence for region r1 */
	private void exitSequence_Distributeur_Distributeur_r1() {
		switch (stateVector[0]) {
			case distributeur_Distributeur_r1_distribution :
				exitSequence_Distributeur_Distributeur_r1_distribution();
				break;

			case distributeur_Distributeur_r1_Selection :
				exitSequence_Distributeur_Distributeur_r1_Selection();
				break;

			case distributeur_Distributeur_r1_Attente :
				exitSequence_Distributeur_Distributeur_r1_Attente();
				break;

			default :
				break;
		}
	}

	/* Default exit sequence for region r1 */
	private void exitSequence_Distributeur_Maintenance_r1() {
		switch (stateVector[0]) {
			case distributeur_Maintenance_r1_StandBy :
				exitSequence_Distributeur_Maintenance_r1_StandBy();
				break;

			default :
				break;
		}
	}

	/* Default exit sequence for region Monayeur */
	private void exitSequence_Monayeur() {
		switch (stateVector[1]) {
			case monayeur_Attente :
				exitSequence_Monayeur_Attente();
				break;

			case monayeur_Paiement :
				exitSequence_Monayeur_Paiement();
				break;

			case monayeur_rendreMonaie :
				exitSequence_Monayeur_rendreMonaie();
				break;

			default :
				break;
		}
	}

	/* The reactions of state distribution. */
	private void react_Distributeur_Distributeur_r1_distribution() {
		if (check_Distributeur_Distributeur_tr0_tr0()) {
			effect_Distributeur_Distributeur_tr0();
		} else {
			if (check_Distributeur_Distributeur_r1_distribution_tr0_tr0()) {
				effect_Distributeur_Distributeur_r1_distribution_tr0();
			}
		}
	}

	/* The reactions of state Selection. */
	private void react_Distributeur_Distributeur_r1_Selection() {
		if (check_Distributeur_Distributeur_tr0_tr0()) {
			effect_Distributeur_Distributeur_tr0();
		} else {
			if (check_Distributeur_Distributeur_r1_Selection_tr0_tr0()) {
				effect_Distributeur_Distributeur_r1_Selection_tr0();
			} else {
				if (check_Distributeur_Distributeur_r1_Selection_tr1_tr1()) {
					effect_Distributeur_Distributeur_r1_Selection_tr1();
				}
			}
		}
	}

	/* The reactions of state Attente. */
	private void react_Distributeur_Distributeur_r1_Attente() {
		if (check_Distributeur_Distributeur_tr0_tr0()) {
			effect_Distributeur_Distributeur_tr0();
		} else {
			if (check_Distributeur_Distributeur_r1_Attente_tr0_tr0()) {
				effect_Distributeur_Distributeur_r1_Attente_tr0();
			}
		}
	}

	/* The reactions of state StandBy. */
	private void react_Distributeur_Maintenance_r1_StandBy() {
		if (check_Distributeur_Maintenance_r1_StandBy_tr0_tr0()) {
			effect_Distributeur_Maintenance_r1_StandBy_tr0();
		} else {
			if (check_Distributeur_Maintenance_r1_StandBy_tr1_tr1()) {
				effect_Distributeur_Maintenance_r1_StandBy_tr1();
			}
		}
	}

	/* The reactions of state Attente. */
	private void react_Monayeur_Attente() {
		if (check_Monayeur_Attente_tr0_tr0()) {
			effect_Monayeur_Attente_tr0();
		}
	}

	/* The reactions of state Paiement. */
	private void react_Monayeur_Paiement() {
		if (check_Monayeur_Paiement_tr0_tr0()) {
			effect_Monayeur_Paiement_tr0();
		} else {
			if (check_Monayeur_Paiement_tr1_tr1()) {
				effect_Monayeur_Paiement_tr1();
			}
		}
	}

	/* The reactions of state rendreMonaie. */
	private void react_Monayeur_rendreMonaie() {
		if (check_Monayeur_rendreMonaie_tr0_tr0()) {
			effect_Monayeur_rendreMonaie_tr0();
		}
	}

	/* Default react sequence for initial entry  */
	private void react_Distributeur_Distributeur_r1__entry_Default() {
		enterSequence_Distributeur_Distributeur_r1_Attente_default();
	}

	/* Default react sequence for shallow history entry hist */
	private void react_Distributeur_Distributeur_r1_hist() {
		/* Enter the region with shallow history */
		if (historyVector[0] != State.$NullState$) {
			shallowEnterSequence_Distributeur_Distributeur_r1();
		}
	}

	/* Default react sequence for initial entry  */
	private void react_Distributeur__entry_Default() {
		enterSequence_Distributeur_Distributeur_default();
	}

	/* Default react sequence for initial entry  */
	private void react_Distributeur_Maintenance_r1__entry_Default() {
		enterSequence_Distributeur_Maintenance_r1_StandBy_default();
	}

	/* Default react sequence for initial entry  */
	private void react_Monayeur__entry_Default() {
		enterSequence_Monayeur_Attente_default();
	}

	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The statemachine needs to be initialized first by calling the init() function.");

		clearOutEvents();

		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {

			switch (stateVector[nextStateIndex]) {
				case distributeur_Distributeur_r1_distribution :
					react_Distributeur_Distributeur_r1_distribution();
					break;
				case distributeur_Distributeur_r1_Selection :
					react_Distributeur_Distributeur_r1_Selection();
					break;
				case distributeur_Distributeur_r1_Attente :
					react_Distributeur_Distributeur_r1_Attente();
					break;
				case distributeur_Maintenance_r1_StandBy :
					react_Distributeur_Maintenance_r1_StandBy();
					break;
				case monayeur_Attente :
					react_Monayeur_Attente();
					break;
				case monayeur_Paiement :
					react_Monayeur_Paiement();
					break;
				case monayeur_rendreMonaie :
					react_Monayeur_rendreMonaie();
					break;
				default :
					// $NullState$
			}
		}

		clearEvents();
	}
}
