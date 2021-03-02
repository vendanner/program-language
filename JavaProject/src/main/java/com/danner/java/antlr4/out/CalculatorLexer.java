// Generated from /Users/luyongtao/vendanner/learn/java/JavaProject/src/main/java/com/danner/java/antlr4/Calculator.g4 by ANTLR 4.8
package com.danner.java.antlr4.out;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CalculatorLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, WS=3, FLOAT=4, MUL=5, DIV=6, ADD=7, SUB=8;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "WS", "FLOAT", "DIGIT", "EXPONENT", "MUL", "DIV", "ADD", 
			"SUB"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", null, null, "'*'", "'/'", "'+'", "'-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "WS", "FLOAT", "MUL", "DIV", "ADD", "SUB"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CalculatorLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Calculator.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\nW\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\3\2\3\2\3\3\3\3\3\4\6\4\35\n\4\r\4\16\4\36\3\4\3\4\3\5\6\5$\n\5\r"+
		"\5\16\5%\3\5\3\5\7\5*\n\5\f\5\16\5-\13\5\3\5\5\5\60\n\5\3\5\3\5\6\5\64"+
		"\n\5\r\5\16\5\65\3\5\5\59\n\5\3\5\6\5<\n\5\r\5\16\5=\3\5\5\5A\n\5\5\5"+
		"C\n\5\3\6\3\6\3\7\3\7\5\7I\n\7\3\7\6\7L\n\7\r\7\16\7M\3\b\3\b\3\t\3\t"+
		"\3\n\3\n\3\13\3\13\2\2\f\3\3\5\4\7\5\t\6\13\2\r\2\17\7\21\b\23\t\25\n"+
		"\3\2\5\5\2\13\f\17\17\"\"\4\2GGgg\4\2--//\2`\2\3\3\2\2\2\2\5\3\2\2\2\2"+
		"\7\3\2\2\2\2\t\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2"+
		"\2\2\3\27\3\2\2\2\5\31\3\2\2\2\7\34\3\2\2\2\tB\3\2\2\2\13D\3\2\2\2\rF"+
		"\3\2\2\2\17O\3\2\2\2\21Q\3\2\2\2\23S\3\2\2\2\25U\3\2\2\2\27\30\7*\2\2"+
		"\30\4\3\2\2\2\31\32\7+\2\2\32\6\3\2\2\2\33\35\t\2\2\2\34\33\3\2\2\2\35"+
		"\36\3\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37 \3\2\2\2 !\b\4\2\2!\b\3\2\2"+
		"\2\"$\5\13\6\2#\"\3\2\2\2$%\3\2\2\2%#\3\2\2\2%&\3\2\2\2&\'\3\2\2\2\'+"+
		"\7\60\2\2(*\5\13\6\2)(\3\2\2\2*-\3\2\2\2+)\3\2\2\2+,\3\2\2\2,/\3\2\2\2"+
		"-+\3\2\2\2.\60\5\r\7\2/.\3\2\2\2/\60\3\2\2\2\60C\3\2\2\2\61\63\7\60\2"+
		"\2\62\64\5\13\6\2\63\62\3\2\2\2\64\65\3\2\2\2\65\63\3\2\2\2\65\66\3\2"+
		"\2\2\668\3\2\2\2\679\5\r\7\28\67\3\2\2\289\3\2\2\29C\3\2\2\2:<\5\13\6"+
		"\2;:\3\2\2\2<=\3\2\2\2=;\3\2\2\2=>\3\2\2\2>@\3\2\2\2?A\5\r\7\2@?\3\2\2"+
		"\2@A\3\2\2\2AC\3\2\2\2B#\3\2\2\2B\61\3\2\2\2B;\3\2\2\2C\n\3\2\2\2DE\4"+
		"\62;\2E\f\3\2\2\2FH\t\3\2\2GI\t\4\2\2HG\3\2\2\2HI\3\2\2\2IK\3\2\2\2JL"+
		"\5\13\6\2KJ\3\2\2\2LM\3\2\2\2MK\3\2\2\2MN\3\2\2\2N\16\3\2\2\2OP\7,\2\2"+
		"P\20\3\2\2\2QR\7\61\2\2R\22\3\2\2\2ST\7-\2\2T\24\3\2\2\2UV\7/\2\2V\26"+
		"\3\2\2\2\16\2\36%+/\658=@BHM\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}