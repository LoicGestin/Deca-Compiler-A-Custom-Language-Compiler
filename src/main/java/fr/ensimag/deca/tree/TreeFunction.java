package fr.ensimag.deca.tree;

/**
 * Function that takes a tree as argument.
 *
 * @author gl29
 * @date 01/01/2024
 * @see fr.ensimag.deca.tree.Tree#iter(TreeFunction)
 */
public interface TreeFunction {
    void apply(Tree t);
}
