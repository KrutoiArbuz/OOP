package ru.nsu.masolygin.Expressions;

/**
 * Вычитание двух выражений.
 */
public class Sub extends BinaryExpression {

    /**
     * Создает вычитание.
     *
     * @param left левое выражение
     * @param right правое выражение
     */
    public Sub(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Печатает вычитание.
     */
    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("-");
        right.print();
        System.out.print(")");
    }

    /**
     * Вычисляет производную вычитания.
     *
     * @param variable переменная
     * @return производная
     */
    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable), right.derivative(variable)).simplify();
    }

    /**
     * Вычисляет значение вычитания.
     *
     * @param assignments значения переменных
     * @return результат
     */
    @Override
    public int eval(String assignments) {
        return left.eval(assignments) - right.eval(assignments);
    }

    /**
     * Упрощает вычитание.
     *
     * @return упрощенное выражение
     */
    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            int result = ((Number) simplifiedLeft).getValue()
                - ((Number) simplifiedRight).getValue();
            return new Number(result);
        }

        if (areExpressionsEqual(simplifiedLeft, simplifiedRight)) {
            return new Number(0);
        }

        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 0) {
            return simplifiedLeft;
        }

        return new Sub(simplifiedLeft, simplifiedRight);
    }

    /**
     * Проверяет равенство двух выражений.
     *
     * @param expr1 первое выражение
     * @param expr2 второе выражение
     * @return true если выражения равны
     */
    private boolean areExpressionsEqual(Expression expr1, Expression expr2) {
        if (expr1.getClass() != expr2.getClass()) {
            return false;
        }

        if (expr1 instanceof Number) {
            return ((Number) expr1).getValue() == ((Number) expr2).getValue();
        }

        if (expr1 instanceof Variable) {
            return ((Variable) expr1).getName().equals(((Variable) expr2).getName());
        }

        if (expr1 instanceof Add) {
            Add add1 = (Add) expr1;
            Add add2 = (Add) expr2;
            return areExpressionsEqual(add1.getLeft(), add2.getLeft())
                && areExpressionsEqual(add1.getRight(), add2.getRight());
        }

        if (expr1 instanceof Sub) {
            Sub sub1 = (Sub) expr1;
            Sub sub2 = (Sub) expr2;
            return areExpressionsEqual(sub1.getLeft(), sub2.getLeft())
                && areExpressionsEqual(sub1.getRight(), sub2.getRight());
        }

        if (expr1 instanceof Mul) {
            Mul mul1 = (Mul) expr1;
            Mul mul2 = (Mul) expr2;
            return areExpressionsEqual(mul1.getLeft(), mul2.getLeft())
                && areExpressionsEqual(mul1.getRight(), mul2.getRight());
        }

        if (expr1 instanceof Div) {
            Div div1 = (Div) expr1;
            Div div2 = (Div) expr2;
            return areExpressionsEqual(div1.getLeft(), div2.getLeft())
                && areExpressionsEqual(div1.getRight(), div2.getRight());
        }

        return false;
    }
}
