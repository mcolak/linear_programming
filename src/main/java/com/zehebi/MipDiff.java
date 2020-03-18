package com.zehebi;

/*
This example formulates and solves the following simple MIP model:

     minimize    20∑x  +   25∑y + 3 ∑z

*/

import gurobi.*;

public class MipDiff {
    public static void main(String[] args) {
        try {

            // Create empty environment, set options, and start
            GRBEnv env = new GRBEnv(true);
            //env.set("logFile", "mip1.log");
            env.start();

            // Create empty model
            GRBModel model = new GRBModel(env);

            // Create variables
            GRBVar[] x = new GRBVar[4];
            GRBVar[] y = new GRBVar[4];
            for (int i = 0; i < 4; i++) {
                x[i] = model.addVar(0, 800, 0.0, GRB.CONTINUOUS, "x" + i);
                y[i] = model.addVar(0, 200, 0.0, GRB.CONTINUOUS, "y" + i);
            }
            GRBVar[] iz = new GRBVar[3];

            for (int i = 0; i < 3; i++) {
                iz[i] = model.addVar(0, 200, 0.0, GRB.CONTINUOUS, "iz" + i);
            }


            // Set objective: maximize x + y + 2 z
            GRBLinExpr expr = new GRBLinExpr();


            // Add constraint: x +  y - iz >= 1000
            expr = new GRBLinExpr();
            expr.addTerm(1.0, x[0]);
            expr.addTerm(1.0, y[0]);
            expr.addTerm(-1.0, iz[0]);
            model.addConstr(expr, GRB.GREATER_EQUAL, 1000.0, "c0");

            // Add constraint: x + y + iz[0] - iz[1] >= 800
            expr = new GRBLinExpr();
            expr.addTerm(1.0, x[1]);
            expr.addTerm(1.0, y[1]);
            expr.addTerm(1.0, iz[0]);
            expr.addTerm(-1.0, iz[1]);
            model.addConstr(expr, GRB.GREATER_EQUAL, 800, "c1");

            // Add constraint: x + y + iz[1] - iz[2] >= 1200
            expr = new GRBLinExpr();
            expr.addTerm(1.0, x[2]);
            expr.addTerm(1.0, y[2]);
            expr.addTerm(1.0, iz[1]);
            expr.addTerm(-1.0, iz[2]);
            model.addConstr(expr, GRB.GREATER_EQUAL, 1200, "c2");

            // Add constraint: x + y + iz[2] >= 900
            expr = new GRBLinExpr();
            expr.addTerm(1.0, x[3]);
            expr.addTerm(1.0, y[3]);
            expr.addTerm(1.0, iz[2]);
            model.addConstr(expr, GRB.GREATER_EQUAL, 900, "c3");
            expr = new GRBLinExpr();
            for (int i = 0; i < 4; i++) {
                expr.addTerm(20.0, x[i]);
                expr.addTerm(25.0, y[i]);
            }
            for (int i = 0; i < 3; i++) {
                expr.addTerm(3, iz[i]);
            }
            model.setObjective(expr, GRB.MINIMIZE);

            // Optimize model
            model.optimize();
            for (int i = 0; i < 4; i++) {
                System.out.println(x[i].get(GRB.StringAttr.VarName)
                        + " " + x[i].get(GRB.DoubleAttr.X));

                System.out.println(y[i].get(GRB.StringAttr.VarName)
                        + " " + y[i].get(GRB.DoubleAttr.X));
            }
            for (int i = 0; i < 3; i++) {
                System.out.println(iz[i].get(GRB.StringAttr.VarName)
                        + " " + iz[i].get(GRB.DoubleAttr.X));
            }
            System.out.println("Obj: " + model.get(GRB.DoubleAttr.ObjVal));

            // Dispose of model and environment
            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }
}