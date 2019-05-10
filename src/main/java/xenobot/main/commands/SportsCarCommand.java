package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;

import java.util.List;
import java.util.Random;

public class SportsCarCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            String brand = getCarBrands();
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You own a **" + getCarColor() + " " + getCarYear() + " "
                    + brand + " " + getCarModel(brand) + "**").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Gets a random sports car make and model.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "sportscar";
    }

    private int getCarYear() {

        final int[] year = {2020, 2019, 2018, 2017, 2016, 2015};
        Random random = new Random();
        int index = random.nextInt(year.length);

        return year[index];
    }
    private String getCarColor() {

        final String[] colors = {"Red", "Black", "Orange", "White", "Brown", "Grey", "Yellow", "Blue", "Blacked Out"};
        Random random = new Random();
        int index = random.nextInt(colors.length);

        return colors[index];
    }
    private String getCarBrands() {

        final String[] brand = {"McLaren", "BMW", "Subaru", "Mazda", "Porsche", "Chevrolet", "Ford",
                "Nissan", "Audi", "Jaguar", "Dodge"};
        Random random = new Random();
        int index = random.nextInt(brand.length);

        return brand[index];
    }
    private String getCarModel(String brand) {
        if (brand.equalsIgnoreCase("mclaren")) {
            final String[] model = {"600LT Spider", "600LT", "570S Coupé", "570S Spider", "570GT", "540C"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        } else if (brand.equalsIgnoreCase("bmw")) {
            final String[] model = {"M2", "M3", "M4", "M5", "M6", "2 Series", "6 Series", "i8", "M240",
                    "M550", "M760"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        } else if (brand.equalsIgnoreCase("subaru")) {
            final String[] model = {"BRZ", "WRX"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        } else if (brand.equalsIgnoreCase("mazda")) {
            final String[] model = {"MX-5 Miata RF", "MX-5 Miata"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        } else if (brand.equalsIgnoreCase("porsche")) {
            final String[] model = {"911", "718 Boxster", "718 Cayman"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        } else if (brand.equalsIgnoreCase("chevrolet")) {
            final String[] model = {"Corvette", "Camaro"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        } else if (brand.equalsIgnoreCase("ford")) {
            final String[] model = {"Mustang", "Shelby GT350"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        } else if (brand.equalsIgnoreCase("nissan")) {
            final String[] model = {"GT-R", "370Z"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        } else if (brand.equalsIgnoreCase("audi")) {
            final String[] model = {"R8", "TT", "S4", "S5", "A5", "RS 5", "S6", "TT RS", "RS 7", "RS 3",
                    "S8", "S3", "TTS", "S7"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        } else if (brand.equalsIgnoreCase("jaguar")) {
            final String[] model = {"F-Type", "F-Type Convertible"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        } else if (brand.equalsIgnoreCase("dodge")) {
            final String[] model = {"Viper"};
            Random random = new Random();
            int index = random.nextInt(model.length);

            return model[index];
        }
        return "Unknown";
    }
}
