package org.firstinspires.ftc.teamcode;

import static org.junit.Assert.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
// import org.junit.jupiter.api.Test;

public class SillierTests {
    @Test
    public void additionTest() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void additionFailTest() {
        assertEquals(3, 2 + 2);
    }
}
