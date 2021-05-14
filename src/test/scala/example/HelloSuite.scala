package example

class HelloSuite extends munit.FunSuite {
  test("The Hello object should say hello.") {
    assertEquals(Hello.greeting, "hello")
  }
}
