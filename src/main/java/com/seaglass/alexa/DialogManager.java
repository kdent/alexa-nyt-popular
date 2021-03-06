package com.seaglass.alexa;

public class DialogManager {

    public static State getNextState(DialogContext dialogContext, Symbol currentSymbol) {
        State currentState = dialogContext.getCurrentState();
        State nextState = null;
        if (dialogContext.getLastStartingItem() > dialogContext.getListLength()) {
            nextState = State.INIT;
        } else {
            nextState = transitionTable[currentState.ordinal()][currentSymbol.ordinal()];
        }
        return nextState;
    }

    public static Symbol getSymbol(String intent) {
        Symbol symbol = null;
        if (intent.equals("Launch")) {
          symbol = Symbol.Launch;
        } else if (intent.equals("StartList") || intent.equals("AMAZON.StartOverIntent") || intent.equals("AMAZON.RepeatIntent")) {
            symbol = Symbol.ReadSection;
        } else if (intent.equals("Request")) {
            symbol = Symbol.RequestList;
        } else if (intent.equals("AMAZON.YesIntent")) {
            symbol = Symbol.Yes;
        } else if (intent.equals("AMAZON.NoIntent") || intent.equals("AMAZON.CancelIntent") || intent.equals("AMAZON.StopIntent")) {
            symbol = Symbol.No;
        } else if (intent.equals("AMAZON.HelpIntent")) {
            symbol = Symbol.Help;
        }
        return symbol;
    }

    public static State getState(String stateName) {
        State state = null;
        if (stateName == null)
            return null;
        if (stateName.equals("INIT")) {
            state = State.INIT;
        } else if (stateName.equals("LAUNCH")) {
            state = State.LAUNCH;
        } else if (stateName.equals("REQUEST")) {
            state = State.REQUEST;
        } else if (stateName.equals("DELIVER_LIST")) {
            state = State.DELIVER_LIST;
        } else if (stateName.equals("HELP")) {
            state = State.HELP;
        } else if (stateName.equals("END")) {
            state = State.END;
        }
        return state;
    }

    public enum State {
        INIT,       // starting state
        LAUNCH,     // User has launched the skill
        REQUEST,    // User has requested headlines without giving a section
        DELIVER_LIST,    // User has requested headlines with a section or has asked to continue a list that has been started
        HELP,       // User has asked for help with the skill
        END     // An error condition
    }

    public enum Symbol {
        Launch,
        RequestList,
        ReadSection,
        Yes,
        No,
        Help
    }

    private static State[][] transitionTable = {
        // Launch      RequestList    StartList           Yes                 No          Help
        {State.LAUNCH, State.REQUEST, State.DELIVER_LIST, State.REQUEST,      State.END, State.HELP},    // INIT
        {State.LAUNCH, State.REQUEST, State.DELIVER_LIST, State.REQUEST,      State.END, State.HELP},    // LAUNCH
        {State.LAUNCH, State.REQUEST, State.DELIVER_LIST, State.REQUEST,      State.END, State.HELP},    // REQUEST
        {State.LAUNCH, State.REQUEST, State.DELIVER_LIST, State.DELIVER_LIST, State.END, State.HELP},    // IN_LIST
        {State.LAUNCH, State.REQUEST, State.DELIVER_LIST, State.REQUEST,      State.END, State.INIT},    // HELP
        {State.LAUNCH, State.REQUEST, State.DELIVER_LIST, State.INIT,         State.END, State.HELP}     // END
    };

}
