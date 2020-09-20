package forestry.core.commands;


import java.util.Optional;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import genetics.api.GeneticsAPI;
import genetics.api.individual.IChromosome;
import genetics.api.individual.IChromosomeType;
import genetics.api.individual.IGenome;
import genetics.api.individual.IIndividual;

public class CommandListAlleles {

	public static ArgumentBuilder<CommandSource, ?> register() {
		return Commands.literal("listAlleles").requires(PermLevel.ADMIN).executes(CommandListAlleles::execute);
	}

	public static int execute(CommandContext<CommandSource> context) throws CommandException, CommandSyntaxException {
		PlayerEntity player = context.getSource().asPlayer();

		ItemStack stack = player.getHeldItemMainhand();

		Optional<IIndividual> individual = GeneticsAPI.apiInstance.getRootHelper().getIndividual(stack);
		if (!individual.isPresent()) {
			return 0;
		}

		IGenome genome = individual.get().getGenome();

		for (IChromosome chromosome : genome.getChromosomes()) {
			IChromosomeType type = chromosome.getType();

			CommandHelpers.sendChatMessage(context.getSource(), type.getName() + ": " + genome.getActiveAllele(type).getDisplayName().getString() + " " + genome.getInactiveAllele(type).getDisplayName().getString());
		}

		return 1;
	}
}
