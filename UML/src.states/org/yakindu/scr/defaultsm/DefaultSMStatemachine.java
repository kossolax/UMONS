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

		public void clearEvents() {
			insertPiece = false;
			addItem = false;
			refound = false;
			maintenance = false;
		}

	}

	private SCInterfaceImpl sCInterface;

	public enum State {
		distributeur_Distributeur, distributeur_Distributeur_r1_distribution, distributeur_Distributeur_r1_Selection, distributeur_Distributeur_r1_Attente, distributeur_Maintenance, distributeur_Maintenance_r1_StandBy, monayeur_Attente, monayeur_Paiement, monayeur_rendreMonaie, $NullState$
	};

	private double totalMoney;
	private double totalPaid;
	private double needMoney;
	private boolean enabled;

	private State[] historyVector = new State[1];
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

		for (int i = 0; i < 1; i++) {
			historyVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();

		sCInterface.piece = 0.5;

		sCInterface.itemPrice = 1;

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

		needMoney = 0;

		nextStateIndex = 0;
		stateVector[0] = State.distributeur_Distributeur_r1_Attente;

		historyVector[0] = stateVector[0];

		nextStateIndex = 1;
		stateVector[1] = State.monayeur_Attente;
	}

	public void exit() {
		switch (stateVector[0]) {
			case distributeur_Distributeur_r1_distribution :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				timer.unsetTimer(this, 0);

				sCInterface.raiseRefound();
				break;

			case distributeur_Distributeur_r1_Selection :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				timer.unsetTimer(this, 1);
				break;

			case distributeur_Distributeur_r1_Attente :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;
				break;

			case distributeur_Maintenance_r1_StandBy :
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;
				break;

			default :
				break;
		}

		switch (stateVector[1]) {
			case monayeur_Attente :
				nextStateIndex = 1;
				stateVector[1] = State.$NullState$;
				break;

			case monayeur_Paiement :
				nextStateIndex = 1;
				stateVector[1] = State.$NullState$;
				break;

			case monayeur_rendreMonaie :
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
			case distributeur_Distributeur :
				return stateVector[0].ordinal() >= State.distributeur_Distributeur
						.ordinal()
						&& stateVector[0].ordinal() <= State.distributeur_Distributeur_r1_Attente
								.ordinal();
			case distributeur_Distributeur_r1_distribution :
				return stateVector[0] == State.distributeur_Distributeur_r1_distribution;
			case distributeur_Distributeur_r1_Selection :
				return stateVector[0] == State.distributeur_Distributeur_r1_Selection;
			case distributeur_Distributeur_r1_Attente :
				return stateVector[0] == State.distributeur_Distributeur_r1_Attente;
			case distributeur_Maintenance :
				return stateVector[0].ordinal() >= State.distributeur_Maintenance
						.ordinal()
						&& stateVector[0].ordinal() <= State.distributeur_Maintenance_r1_StandBy
								.ordinal();
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

	/* Entry action for statechart 'default'. */
	private void entryAction() {
	}

	/* Exit action for state 'default'. */
	private void exitAction() {
	}

	/* shallow enterSequence with history in child r1 */
	private void shallowEnterSequenceDistributeur_Distributeur_r1() {
		switch (historyVector[0]) {
			case distributeur_Distributeur_r1_distribution :

				timer.setTimer(this, 0, 1 * 1000, false);

				totalPaid -= needMoney;

				nextStateIndex = 0;
				stateVector[0] = State.distributeur_Distributeur_r1_distribution;

				historyVector[0] = stateVector[0];
				break;

			case distributeur_Distributeur_r1_Selection :

				timer.setTimer(this, 1, 10 * 1000, false);

				needMoney += sCInterface.itemPrice;

				nextStateIndex = 0;
				stateVector[0] = State.distributeur_Distributeur_r1_Selection;

				historyVector[0] = stateVector[0];
				break;

			case distributeur_Distributeur_r1_Attente :
				needMoney = 0;

				nextStateIndex = 0;
				stateVector[0] = State.distributeur_Distributeur_r1_Attente;

				historyVector[0] = stateVector[0];
				break;

			default :
				break;
		}
	}

	/* The reactions of state distribution. */
	private void reactDistributeur_Distributeur_r1_distribution() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case distributeur_Distributeur_r1_distribution :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 0);

					sCInterface.raiseRefound();
					break;

				case distributeur_Distributeur_r1_Selection :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 1);
					break;

				case distributeur_Distributeur_r1_Attente :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				default :
					break;
			}

			sCInterface.raiseRefound();

			nextStateIndex = 0;
			stateVector[0] = State.distributeur_Maintenance_r1_StandBy;
		} else {
			if (timeEvents[0]) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				timer.unsetTimer(this, 0);

				sCInterface.raiseRefound();

				needMoney = 0;

				nextStateIndex = 0;
				stateVector[0] = State.distributeur_Distributeur_r1_Attente;

				historyVector[0] = stateVector[0];
			}
		}
	}

	/* The reactions of state Selection. */
	private void reactDistributeur_Distributeur_r1_Selection() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case distributeur_Distributeur_r1_distribution :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 0);

					sCInterface.raiseRefound();
					break;

				case distributeur_Distributeur_r1_Selection :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 1);
					break;

				case distributeur_Distributeur_r1_Attente :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				default :
					break;
			}

			sCInterface.raiseRefound();

			nextStateIndex = 0;
			stateVector[0] = State.distributeur_Maintenance_r1_StandBy;
		} else {
			if (totalPaid >= needMoney) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				timer.unsetTimer(this, 1);

				timer.setTimer(this, 0, 1 * 1000, false);

				totalPaid -= needMoney;

				nextStateIndex = 0;
				stateVector[0] = State.distributeur_Distributeur_r1_distribution;

				historyVector[0] = stateVector[0];
			} else {
				if (timeEvents[1]) {
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 1);

					needMoney = 0;

					nextStateIndex = 0;
					stateVector[0] = State.distributeur_Distributeur_r1_Attente;

					historyVector[0] = stateVector[0];
				}
			}
		}
	}

	/* The reactions of state Attente. */
	private void reactDistributeur_Distributeur_r1_Attente() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case distributeur_Distributeur_r1_distribution :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 0);

					sCInterface.raiseRefound();
					break;

				case distributeur_Distributeur_r1_Selection :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;

					timer.unsetTimer(this, 1);
					break;

				case distributeur_Distributeur_r1_Attente :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				default :
					break;
			}

			sCInterface.raiseRefound();

			nextStateIndex = 0;
			stateVector[0] = State.distributeur_Maintenance_r1_StandBy;
		} else {
			if (sCInterface.addItem) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				timer.setTimer(this, 1, 10 * 1000, false);

				needMoney += sCInterface.itemPrice;

				nextStateIndex = 0;
				stateVector[0] = State.distributeur_Distributeur_r1_Selection;

				historyVector[0] = stateVector[0];
			}
		}
	}

	/* The reactions of state StandBy. */
	private void reactDistributeur_Maintenance_r1_StandBy() {
		if (sCInterface.maintenance) {
			switch (stateVector[0]) {
				case distributeur_Maintenance_r1_StandBy :
					nextStateIndex = 0;
					stateVector[0] = State.$NullState$;
					break;

				default :
					break;
			}

			/* Enter the region with shallow history */
			if (historyVector[0] != State.$NullState$) {
				shallowEnterSequenceDistributeur_Distributeur_r1();
			}
		} else {
			if (totalPaid > 0) {
				nextStateIndex = 0;
				stateVector[0] = State.$NullState$;

				sCInterface.raiseRefound();

				nextStateIndex = 0;
				stateVector[0] = State.distributeur_Maintenance_r1_StandBy;
			}
		}
	}

	/* The reactions of state Attente. */
	private void reactMonayeur_Attente() {
		if (sCInterface.insertPiece) {
			nextStateIndex = 1;
			stateVector[1] = State.$NullState$;

			totalMoney += sCInterface.piece;

			totalPaid += sCInterface.piece;

			nextStateIndex = 1;
			stateVector[1] = State.monayeur_Paiement;
		}
	}

	/* The reactions of state Paiement. */
	private void reactMonayeur_Paiement() {
		if (sCInterface.insertPiece) {
			nextStateIndex = 1;
			stateVector[1] = State.$NullState$;

			totalMoney += sCInterface.piece;

			totalPaid += sCInterface.piece;

			nextStateIndex = 1;
			stateVector[1] = State.monayeur_Paiement;
		} else {
			if (sCInterface.refound) {
				nextStateIndex = 1;
				stateVector[1] = State.$NullState$;

				totalMoney -= totalPaid;

				totalPaid = 0;

				nextStateIndex = 1;
				stateVector[1] = State.monayeur_rendreMonaie;
			}
		}
	}

	/* The reactions of state rendreMonaie. */
	private void reactMonayeur_rendreMonaie() {
		if (totalPaid == 0) {
			nextStateIndex = 1;
			stateVector[1] = State.$NullState$;

			nextStateIndex = 1;
			stateVector[1] = State.monayeur_Attente;
		}
	}

	public void runCycle() {

		clearOutEvents();

		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {

			switch (stateVector[nextStateIndex]) {
				case distributeur_Distributeur_r1_distribution :
					reactDistributeur_Distributeur_r1_distribution();
					break;
				case distributeur_Distributeur_r1_Selection :
					reactDistributeur_Distributeur_r1_Selection();
					break;
				case distributeur_Distributeur_r1_Attente :
					reactDistributeur_Distributeur_r1_Attente();
					break;
				case distributeur_Maintenance_r1_StandBy :
					reactDistributeur_Maintenance_r1_StandBy();
					break;
				case monayeur_Attente :
					reactMonayeur_Attente();
					break;
				case monayeur_Paiement :
					reactMonayeur_Paiement();
					break;
				case monayeur_rendreMonaie :
					reactMonayeur_rendreMonaie();
					break;
				default :
					// $NullState$
			}
		}

		clearEvents();
	}
}
