package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;

import java.util.List;
import java.util.Random;

public class ShopCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " just bought a **" +
                    getShoppingItems() + "**").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Gets a random item the user has recently bought.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "shop";
    }

    private String getShoppingItems() {

        final String[] items = {"Coca Cola", "Mountain Dew", "Sprite", "Kit Kat", "Ice Cream Sandwich", "Air Compressor",
                "Steak", "Chicken Breast", "Leather Belt", "Crayon Pack", "Pack of Pencils", "Mechanical Pencil", "Pack of Ice",
                "Sweatshirt", "T-Shirt", "Bag of Chips", "Homeless Man McDonalds", "Tuxedo", "Rifle", "Can of Soup", "Tray of Brownies",
                "Notebook", "3 Ring Binder", "TV Remote", "Box of Batteries", "USB Flash Drive", "Pack of Coat Hangers",
                "Pencil Sharpener", "Bin of Protein Powder", "Can of Jelly", "Package of Plastic Silverware", "Kitchen Knife Set",
                "Bag of Oats", "Loaf of Bread", "Box of Garbage Bags", "Can of Air Freshener", "Package of Baby Wipes",
                "Fat Man a Salad", "Girl Weight Loss Pills", "Package of Paper", "Jar of BBQ Sauce", "Toy Dinosaur",
                "Cicada Action Figure", "Gorilla Grodd Action Figure", "Box of Tampons", "Tube of Toothpaste", "Package of Gum",
                "Barbie Doll", "Ugly Girl Proactive", "Skinny Guy Mass Gainer", "Package of Mints", "Tin of Chewing Tobacco",
                "Bag of Recces Pieces", "Zoo", "Museum", "Mall", "Highway", "Root Beer", "Bag of Sour Gummy Worms", "Lightbulb",
                "Bag of Piss", "Homeless Man a House", "Pair of Shoes", "New Car", "New House", "Happy Meal", "Flannel Shirt",
                "Box of Condoms", "Oreo Sundae", "New Microphone", "Chicken", "Chicken Little Toy", "Mushroom", "Pack of Cigarettes"};

        Random random = new Random();
        int index = random.nextInt(items.length);

        return items[index];
    }

}
