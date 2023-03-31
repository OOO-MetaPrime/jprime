package mp.jprime.groovy;

import mp.jprime.groovy.exceptions.JPGroovyRestrictiveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JPGroovyShellTest {
  private final JPGroovyShell shell = JPGroovyShell.newInstance();

  @Test
  void testIntegerPlusInteger() {
    Object result = shell.evaluate("1+1");
    assertEquals(2, result);
  }

  @Test
  void testBooleanInteger() {
    Object result = shell.evaluate("if (1 == 1) return true;");
    assertEquals(true, result);
  }

  @Test
  void testBooleanString() {
    Object result = shell.evaluate("if ('1' == '1') return true;");
    assertEquals(true, result);
  }

  @Test
  void testBooleanExtString() {
    shell.setVariable("x", "Mother");
    Object result = shell.evaluate("if ('Mother' == x) return true;");
    assertEquals(true, result);
  }

  @Test
  void testStringsXplusY() {
    Object result = shell.evaluate("'x'+'y'");
    assertEquals("xy", result);
  }

  @Test
  void testXplusY() {
    shell.setVariable("x", 4);
    shell.setVariable("y", 5);
    Object result = shell.evaluate("x+y");
    assertEquals(9,  result);
  }

  @Test
  void testXplusYeqZ() {
    shell.setVariable("x", 2);
    shell.setVariable("y", 3);
    Object result = shell.evaluate("z=x+y");
    assertEquals(5,  result);
    assertEquals(5,  shell.getVariable("z"));
  }

  @Test
  void testEach() {
    Object result = shell.evaluate("x=1; (0..10).each { i -> x = x + i;}; x;");
    assertEquals(56,  result);
  }

  @Test
  void testJson() {
    shell.setVariable("sJson", "{ \"intList\": [1,2,3,4]}");
    Object result = shell.evaluate("def j = new JsonSlurper().parseText(sJson); j.intList.add(5); new JsonGenerator.Options().build().toJson(j);");
    assertEquals("{\"intList\":[1,2,3,4,5]}",  result);
  }

  @Test
  void testPrintln() {
    assertNotNull(
        Assertions.assertThrows(
            JPGroovyRestrictiveException.class, () -> shell.evaluate("println 'Hello'")
        ).getMessage()
    );
    assertNotNull(
        Assertions.assertThrows(
            JPGroovyRestrictiveException.class, () -> shell.evaluate("(0..100).each { println 'Blah' }")
        ).getMessage()
    );
  }

  @Test
  void testSystemExit() {
    assertNotNull(
        Assertions.assertThrows(
            JPGroovyRestrictiveException.class, () -> shell.evaluate("System.exit(-1)")
        ).getMessage()
    );
    assertNotNull(
        Assertions.assertThrows(
            JPGroovyRestrictiveException.class, () -> shell.evaluate("foo(System.exit(-1))")
        ).getMessage()
    );
    assertNotNull(
        Assertions.assertThrows(
            JPGroovyRestrictiveException.class, () -> shell.evaluate("System.exit(-1)==System.exit(-1)")
        ).getMessage()
    );
    assertNotNull(
        Assertions.assertThrows(
            JPGroovyRestrictiveException.class, () -> shell.evaluate("def x=System.&exit; x(-1)")
        ).getMessage()
    );
  }

  @Test
  void testEvalSystemExit() {
    assertNotNull(
        Assertions.assertThrows(
            JPGroovyRestrictiveException.class, () -> shell.evaluate("Eval.me('System.exit(-1)')")
        ).getMessage()
    );
  }

  @Test
  void testFileDelete() {
    assertNotNull(
        Assertions.assertThrows(
            JPGroovyRestrictiveException.class, () -> shell.evaluate("new File('c:\\myFolder').delete()")
        ).getMessage()
    );
  }
}
