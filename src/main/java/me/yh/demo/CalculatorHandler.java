package me.yh.demo;

import org.apache.thrift.TException;
import shared.SharedStruct;
import tutorial.Calculator;
import tutorial.InvalidOperation;
import tutorial.Work;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/10 0010.
 */
public class CalculatorHandler implements Calculator.Iface {

      private HashMap<Integer, SharedStruct> log;

      public CalculatorHandler() {
            log = new HashMap<Integer, SharedStruct>();
      }

      @Override
      public void ping() throws TException {
            System.out.println("ping");
      }

      @Override
      public int add(int num1, int num2) throws TException {
            return num1 + num2;
      }

      @Override
      public int calculate(int logid, Work work) throws InvalidOperation, TException {
            int val = 0;
            switch (work.op) {
                  case ADD:
                        val = work.num1 + work.num2;
                        break;
                  case SUBTRACT:
                        val = work.num1 - work.num2;
                        break;
                  case MULTIPLY:
                        val = work.num1 * work.num2;
                        break;
                  case DIVIDE:
                        if (work.num2 == 0) {
                              InvalidOperation io = new InvalidOperation();
                              io.whatOp = work.op.getValue();
                              io.why = "Cannot divide by 0";
                              throw io;
                        }
                        val = work.num1 / work.num2;
                        break;
                  default:
                        InvalidOperation io = new InvalidOperation();
                        io.whatOp = work.op.getValue();
                        io.why = "Unknown operation";
                        throw io;
            }

            SharedStruct entry = new SharedStruct();
            entry.key = logid;
            entry.value = Integer.toString(val);
            log.put(logid, entry);

            return val;
      }

      @Override
      public void zip() throws TException {
            System.out.println("zip()");
      }

      @Override
      public SharedStruct getStruct(int key) throws TException {
            System.out.println("getStruct(" + key + ")");
            return log.get(key);
      }
}
