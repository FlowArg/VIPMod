var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

function initializeCoreMod() {
    return {
        'SoundManagerInitTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/sounds/SoundManager',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/Options;)V'
            },
            'transformer': function (mn) {
                for(var i = 0; i < mn.instructions.toArray().length; i++) {
                    var node = mn.instructions.toArray()[i];

                    if(node.getOpcode() == Opcodes.NEW)
                    {
                        node.desc = "fr/flowarg/vip3/client/ass/ASSEngine";
                    }

                    if(node.getOpcode() == Opcodes.INVOKESPECIAL)
                    {
                        if(node.owner == "net/minecraft/client/sounds/SoundEngine") {
                            node.owner = "fr/flowarg/vip3/client/ass/ASSEngine";
                        }
                    }

                    if(node.getOpcode() == Opcodes.PUTFIELD)
                    {
                        if(node.descriptor == "Lnet/minecraft/client/sounds/SoundEngine;") {
                            node.descriptor = "Lfr/flowarg/vip3/client/ass/ASSEngine;";
                        }
                    }
                }
                return mn;
            }
        },
        'MusicManagerStartPlayingTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/sounds/MusicManager',
                'methodName': 'startPlaying',
                'methodDesc': '(Lnet/minecraft/sounds/Music;)V'
            },
            'transformer': function (mn) {
                for(var i = 0; i < mn.instructions.toArray().length; i++) {
                    var node = mn.instructions.toArray()[i];

                    if(node.getOpcode() == Opcodes.INVOKESTATIC)
                    {
                        if(node.owner == "net/minecraft/client/resources/sounds/SimpleSoundInstance") {
                            node.owner = "fr/flowarg/vip3/client/ass/ASSEngine";
                            node.desc = "(Lnet/minecraft/sounds/Music;)Lnet/minecraft/client/resources/sounds/SoundInstance;";
                            mn.instructions.remove(mn.instructions.toArray()[i - 1]);
                        }
                    }
                }
                return mn;
            }
        }
    }
}