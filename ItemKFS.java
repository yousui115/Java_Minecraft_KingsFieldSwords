package net.minecraft.src;
//System.out.println("");

public class ItemKFS extends ItemSword
{
    //���R���X�g���N�^
    public ItemKFS(int i, EnumToolMaterial enumtoolmaterial, boolean rust)
    {
        super(i, enumtoolmaterial);
        enumTM = enumtoolmaterial;
        //weaponDamage = 4 + enumtoolmaterial.getDamageVsEntity() * 2;
        kbAttack = ModLoader.getMinecraftInstance().gameSettings.keyBindAttack;

        isRust = rust;
    }

    @Override
    public int getDamageVsEntity(Entity entity)
    {
        return 4 + enumTM.getDamageVsEntity() * 2;
    }

    //���u���b�N��j�󂵂����̑ϋv���������i���ʏ����j
    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        int nDamage = isRust ? 2 : 0;
        itemstack.damageItem(nDamage, entityliving);
        return true;
    }

    //���G�ɍU���������̑ϋv���������i���ʏ����j
    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
        int nDamage = isRust ? 1 : 0;
        itemstack.damageItem(nDamage, entityliving1);
        return true;
    }

    // ������Ă΂�郁�\�b�h�i���ʏ����j
    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        //���v���C���[�ȊO�Ȃ����Ȃ�
        if (!(entity instanceof EntityPlayer)) {
            return ;
        }

        //����������������Ă��Ă��A�J�����g�ƈႤ�Ə�����Ԃ�
        if (entity instanceof EntityPlayer) {
            if (itemstack != ((EntityPlayer)entity).getCurrentEquippedItem()) {
                return ;
            }
        }

        //System.out.println("ItemKFS onUpdate!");

        // ���ʂ̃A�C�e���Ɏ�����������A�K�[�h����������A���͉���
        if (flag == false && canMagicAction == true || entity.getFlag(4) == false)
        {
            //System.out.println("ItemKFS Cancel MagicAttack!");
            canMagicAction = false;
            isGuard = false;
        }
        
        //System.out.println("ItemKFS kbAttack.pressed == " + kbAttack.pressed);
        
        // ���K�[�h���ɍU���{�^�������Ŗ��͂�����
        if (isGuard == true && kbAttack.pressed == true && canMagicAction == false && canUseMagic == true)
        {
            //System.out.println("ItemKFS Magic Charge!");
            canMagicAction = true;
        }

        // �����@�`���[�W�G�t�F�N�g
        if (canMagicAction == true)
        {
            if (tickParticle-- <= 0)
            {
                for (int idx = 0; idx < 20; idx++)
                ModLoader.getMinecraftInstance().effectRenderer.addEffect(
                                new EntityMagicChargeFX(world, entity.posX,
                                                               entity.posY-0.6D,
                                                               entity.posZ,
                                                                (itemRand.nextDouble() - 0.5D) * 2D,
                                                               -itemRand.nextDouble(),
                                                                (itemRand.nextDouble() - 0.5D) * 2D,
                                                               fR, fG, fB));

                tickParticle = 10;
            }
        }

        // ������\�́i�ʏ����j
        onSwordAbility(itemstack, world, entity);
    }

    // ������\�́i�ʏ����j
    public void onSwordAbility(ItemStack itemstack, World world, Entity entity) {}
    
    // �����͂̐F�i���ʏ����j
    public void setParticleColor(float r, float g, float b)
    {
        fR = r;
        fG = g;
        fB = b;
    }
    
    // ���N���t�g�i���ʏ����j����񂩂��B
    @Override
    public void onCreated(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        isGuard = false;
        canMagicAction = false;
        tickParticle = 0;
    }

    // ���E�N���b�N���������u�ԁi���ʏ����j
    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        //System.out.println("ItemKFS onItemRightClick!");
        isGuard = true;
        tickParticle = 0;
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    // ���E�N���b�N������������u�ԁi���ʏ����j
    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
        //System.out.println("ItemKFS offItemRightClick!");
        isGuard = false;
        if (canMagicAction == true)
        {
            //System.out.println("ItemKFS MagicAttack!!");

            //������U�郂�[�V����
            entityplayer.swingItem();

            //�����@�U���i�ʏ����j
            onMagicAction(itemstack, world, entityplayer, i);
            
            canMagicAction = false;
        }
    }

    // �����@�U���i�ʏ����j
    public void onMagicAction(ItemStack itemstack, World world, EntityPlayer entityplayer, int i) {}

    // ���Ȃ񂩔����i���ʏ����j
    /*public boolean canHarvestBlock(Block block)
    {
        return block.blockID == Block.web.blockID;
    }*/

    //protected int weaponDamage;
    protected KeyBinding kbAttack;
    protected boolean isGuard = false;
    protected boolean canMagicAction = false;
    //protected int fRegeneTime = 0;
    protected int tickParticle = 0;
    
    private final boolean isRust;
    protected float fR = 0.0F;
    protected float fG = 0.0F;
    protected float fB = 0.0F;
    
    protected boolean canUseMagic = true;
    protected EnumToolMaterial enumTM;
}
