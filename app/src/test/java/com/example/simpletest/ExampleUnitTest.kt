package com.example.simpletest

import com.google.testing.compile.Compiler.javac
import com.google.testing.compile.JavaFileObjects
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/*
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

//    fun testCompilation() {
//        javac().compile(
//            JavaFileObjects.forSourceLines(
//                "com.example.simpletest.Foo",
//                """
//                package com.example.simpletest;
//
//                public class Foo {
//                    public int foo() {
//                        return 1;
//                    }
//                }
//            """.trimIndent()
//            )
//
//        ).generatedFiles().first().
//    }

}
