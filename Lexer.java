import java.util.Scanner;
import java.util.function.Function;

// 字句解析器本体
public class Lexer {
    String inputString;
    State[] states = new State[8];
    String resultString;

    protected enum StateType {
        Accept_INT10,
        Accept_INT16,
        Accept_DEC,
        Error
    }

    // 状態クラス
    class State {
        int stateId;
        StateType stateType;
        // 次の文字を受け取り次の状態を返す関数を入れる
        Function<Character, State> nextState;

        State(int id, StateType type) {
            stateId = id;
            stateType = type;
        }

        void setNextState(Function<Character, State> next) {
            this.nextState = next;
        }
    }

    public Lexer(String inputString) {
        this.inputString = inputString;

        // 必要な状態を作成し，状態ID，タイプを設定
        states[0] = new State(0, StateType.Error);
        states[1] = new State(1, StateType.Accept_INT10);
        states[2] = new State(2, StateType.Error);
        states[3] = new State(3, StateType.Error);
        states[4] = new State(4, StateType.Accept_INT16);
        states[5] = new State(5, StateType.Accept_INT10);
        states[6] = new State(6, StateType.Error);
        states[7] = new State(7, StateType.Accept_DEC);

        // 遷移先を決定していく
        states[0].setNextState((i) -> {
            char inputChar = i.charValue();
            if (inputChar == '0') {
                return states[1];
            } else if ('1' <= inputChar && inputChar <= '9') {
                return states[5];
            } else if (inputChar == '.') {
                return states[6];
            } else if ('a' <= inputChar && inputChar <= 'f' ||
                    'A' <= inputChar && inputChar <= 'F') {
                return states[4];
            } else {
                return null;
            }
        });
        states[1].setNextState((i) -> {
            char inputChar = i.charValue();
            if (inputChar == 'x') {
                return states[3];
            } else if (inputChar == '.') {
                return states[7];
            } else if ('1' <= inputChar && inputChar <= '9') {
                return states[2];
            } else if ('a' <= inputChar && inputChar <= 'f' ||
                    'A' <= inputChar && inputChar <= 'F') {
                return states[4];
            } else {
                return null;
            }
        });
        states[2].setNextState((i) -> {
            char inputChar = i.charValue();
            if ('0' <= inputChar && inputChar <= '9') {
                return states[2];
            } else if ('a' <= inputChar && inputChar <= 'f' ||
                    'A' <= inputChar && inputChar <= 'F') {
                return states[4];
            } else {
                return null;
            }
        });
        states[3].setNextState((i) -> {
            char inputChar = i.charValue();
            if ('0' <= inputChar && inputChar <= '9' ||
                    'a' <= inputChar && inputChar <= 'f' ||
                    'A' <= inputChar && inputChar <= 'F') {
                return states[4];
            } else {
                return null;
            }
        });
        states[4].setNextState((i) -> {
            char inputChar = i.charValue();
            if ('0' <= inputChar && inputChar <= '9' ||
                    'a' <= inputChar && inputChar <= 'f' ||
                    'A' <= inputChar && inputChar <= 'F') {
                return states[4];
            } else {
                return null;
            }
        });
        states[5].setNextState((i) -> {
            char inputChar = i.charValue();
            if (inputChar == '.') {
                return states[7];
            } else if ('0' <= inputChar && inputChar <= '9') {
                return states[5];
            } else if ('a' <= inputChar && inputChar <= 'f' ||
                    'A' <= inputChar && inputChar <= 'F') {
                return states[4];
            } else {
                return null;
            }
        });
        states[6].setNextState((i) -> {
            char inputChar = i.charValue();
            if ('0' <= inputChar && inputChar <= '9') {
                return states[7];
            } else {
                return null;
            }
        });
        states[7].setNextState((i) -> {
            char inputChar = i.charValue();
            if ('0' <= inputChar && inputChar <= '9') {
                return states[7];
            } else {
                return null;
            }
        });
    }

    public StateType getToken() {
        State result = _getToken(0, states[0]);
        return result.stateType;
    }

    private State _getToken(int currentIndex, State currentState) {
        if (currentIndex == inputString.length()) {
            resultString = inputString;
            return currentState;
        }

        State nextState = currentState.nextState.apply(inputString.charAt(currentIndex));
        // 遷移先がない場合
        if (nextState == null) {
            resultString = inputString.substring(0, currentIndex);
            return currentState;
        }

        State result = _getToken(currentIndex+1, nextState);
        // 遷移先がエラーだった場合は現在の状態を返す
        if (result.stateType == StateType.Error) {
            resultString = inputString.substring(0, currentIndex);
            return currentState;
        } else {
            return result;
        }
    }

    public String getResultString() {
        return resultString;
    }
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();  /* 1行読み取る */
		Lexer lex = new Lexer(str);
        StateType result;

        result = lex.getToken();

        if (result == StateType.Accept_INT10 |
                result == StateType.Accept_INT16) {
                System.out.println("INT" + lex.getResultString());
        } else if (result == StateType.Accept_DEC) {
                System.out.println("DEC" + lex.getResultString());
        } else {
                System.out.println("ERR");
        }
    }
}
