var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var LdcInsnNode = Java.type('org.objectweb.asm.tree.LdcInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

function initializeCoreMod() {
    return {
        'MinecraftTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'com/mojang/blaze3d/platform/Window',
                'methodName': 'm_85422_',
                'methodDesc': '(Ljava/lang/String;)V'
            },
            'transformer': function (mn) {
                mn.instructions.clear();
                mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                mn.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "com/mojang/blaze3d/platform/Window", ASMAPI.mapField("f_85349_"), "J"));
                mn.instructions.add(new LdcInsnNode("VIP 3 - La revanche du dragon rouge"));
                mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/lwjgl/glfw/GLFW", "glfwSetWindowTitle", "(JLjava/lang/CharSequence;)V"));
                mn.instructions.add(new InsnNode(Opcodes.RETURN));
                return mn;
            }
        }
    }
}