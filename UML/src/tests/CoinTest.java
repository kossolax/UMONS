package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import framework.payement.Coin;

public class CoinTest {
	
	int compute(int[] val, int[] cnt, int[] res, int amount) {
		
		int i = 0;
		int j;
		int tmp;
		while( i < res.length && amount != 0 ) {			
			tmp = amount;
			for(j=0; j<res.length; j++)
				res[j] = 0;
			
			j = i;
			while ( tmp > 0 && j < res.length ) {
				while( j < res.length && (val[j] > tmp || cnt[j]-res[j] <= 0) ) j++;
				
				if( j >= res.length )
					break;
				res[j]++;
				tmp = tmp - val[j];
			}
			
			if( tmp == 0 )
				amount = 0;
			i++;
		}
		return amount;
	}
	@Test(timeout=100)
	public void testCoin1() {
		int[] val = {200, 100, 50, 20, 10, 5, 2, 1};
		int[] cnt = {  0,   1,  1,  6, 0,  0, 4, 1};
		int[] res = {  0,   0,  0,  0, 0,  0, 0, 0};
		
		int left = 110;
		left = compute(val, cnt, res, left);
		
		assertTrue(left == 0);
		assertTrue(res[0] == 0);
		assertTrue(res[1] == 0);
		assertTrue(res[2] == 1);
		assertTrue(res[3] == 3);
		assertTrue(res[4] == 0);
		assertTrue(res[5] == 0);
		assertTrue(res[6] == 0);
		assertTrue(res[7] == 0);
	}
	
	@Test(timeout=100)
	public void testCoin2() {
		int[] val = {200, 100, 50, 20, 10, 5, 2, 1};
		int[] cnt = {  0,   0,  0,  0, 0,  0, 0, 0};
		int[] res = {  0,   0,  0,  0, 0,  0, 0, 0};
		
		int left = 110;
		left = compute(val, cnt, res, left);
		
		assertTrue(left == 110);
		for(int i=0; i<=7; i++)
			assertTrue(res[i] == cnt[i] && cnt[i] == 0);
	}
	@Test(timeout=100)
	public void testCoin3() {
		int[] val = {200, 100, 50, 20, 10, 5, 2, 1};
		int[] cnt = {  0,   0,  0,  0, 0,  0, 0, 1};
		int[] res = {  0,   0,  0,  0, 0,  0, 0, 0};
		
		int left = 110;
		left = compute(val, cnt, res, left);
		
		assertTrue(left == 110);
		for(int i=0; i<=6; i++)
			assertTrue(res[i] == cnt[i] && cnt[i] == 0);
		assertTrue(res[7] == 1);
	}
	@Test(timeout=100)
	public void testCoin4() {
		int[] val = {200, 100, 50, 20, 10, 5, 2, 1};
		int[] cnt = {  9,   9,  9,  9, 9,  9, 9, 9};
		int[] res = {  0,   0,  0,  0, 0,  0, 0, 0};
			
		int left = 101;
		left = compute(val, cnt, res, left);
		assertTrue(left == 0);
	}
	@Test(timeout=1000)
	public void testCoin5() {
		int[] val = {200, 100, 50, 20, 10, 5, 2, 1};
		int[] cnt = { 50,  50, 50, 50, 50, 50,50,50};
		
		for(int i = 0; i<=10000; i++) {
			
			int[] res = {  0,   0,  0,  0, 0,  0, 0, 0};
			
			int left = i;
			left = compute(val, cnt, res, left);
			assertTrue(left == 0);
		}
	}
	@Test(timeout=1000)
	public void testCoin6() {
		framework.Machine machine = new framework.Machine("hello");
		Coin c = new Coin(machine);
		c.addModule(1);
		c.addModule(2);
		c.addModule(5);
		c.addModule(10);
		c.addModule(20);
		c.addModule(50);
		c.addModule(100);
		c.addModule(200);
		machine.addModule( c );
		
		c.insertPiece(100);
		c.insertPiece(50);
		c.insertPiece(20);c.insertPiece(20);c.insertPiece(20);
		c.insertPiece(20);c.insertPiece(20);c.insertPiece(20);
		c.insertPiece(2);c.insertPiece(2);
		c.insertPiece(2);c.insertPiece(2);
		c.insertPiece(1);
		
		
		ArrayList<Integer> res = c.refund(110);
		assertTrue( res.size() == 4 );
		int sum = 0;
		for( Integer a : res ) {
			sum += a;
		}
		assertTrue( sum == 110 );
	}
	

}
