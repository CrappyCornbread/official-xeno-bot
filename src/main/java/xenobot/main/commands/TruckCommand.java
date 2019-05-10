package xenobot.main.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xenobot.main.Constants;
import xenobot.main.objects.ICommand;

import java.util.List;
import java.util.Random;

public class TruckCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            String brand = getTruckBrands();
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You own a **" + getTruckColor() + " " +
                    getTruckYear() + " " + brand + " " + getTruckModel(brand) + "**").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Gets a random truck make and model.\n\n" +
                "__**Usage:**__ `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "truck";
    }

    public int getTruckYear() {

        final int[] year = {2019, 2018, 2017, 2016, 2015, 2014, 2013, 2012, 2011, 2010, 2009, 2008,
                2007, 2006, 2005};
        Random random = new Random();
        int index = random.nextInt(year.length);

        return year[index];
    }

    public String getTruckBrands() {

        final String[] brands = {"Ford", "Toyota", "Chevy", "Dodge", "Honda", "GMC", "Nissan"};
        Random random = new Random();
        int index = random.nextInt(brands.length);

        return brands[index];
    }

    public String getTruckColor() {

        final String[] colors = {"Red", "Black", "Orange", "White", "Brown", "Grey", "Yellow", "Blue"};
        Random random = new Random();
        int index = random.nextInt(colors.length);
        String color = colors[index];

        if (color.equalsIgnoreCase("black")) {
            final String[] blackColors = {"Blacked Out", "Black"};
            int blackIndex = random.nextInt(colors.length);
            String blackColor = blackColors[blackIndex];

            return blackColor;
        }
        return color;
    }
    public String getTruckModel(String brand) {

        if (brand.equalsIgnoreCase("ford")) {

            final String[] models = {"F150", "F250", "F350", "Ranger"};

            Random random = new Random();
            int index = random.nextInt(models.length);
            String model = models[index];
            final String[] subModels = {"", "Platinum", "Limited", "King Ranch"};
            int subIndex = random.nextInt(subModels.length);
            String subModel = subModels[subIndex];

            //If Ranger, can't be a sub-model because there aren't any
            //Only Ford F150 has a Raptor sub-model
            if (model.equalsIgnoreCase("Ranger")) {
                return model;
            } else if (model.equalsIgnoreCase("F150")) {
                final String[] F150SubModels = {"", "Platinum", "Limited", "King Ranch", "Raptor"};
                int F150SubIndex = random.nextInt(subModels.length);
                String F150SubModel = F150SubModels[F150SubIndex];
                return model + " " + F150SubModel;
            } else {
                return  model + " " + subModel;
            }

        } else if (brand.equalsIgnoreCase("toyota")) {

            final String[] models = {"Tacoma", "Tundra"};
            Random random = new Random();
            int index = random.nextInt(models.length);
            return models[index];

        } else if (brand.equalsIgnoreCase("chevy")) {

            final String[] models = {"Colorado", "Silverado"};
            Random random = new Random();
            int index = random.nextInt(models.length);
            String model = models[index];

            final String[] subModels = {"1500", "2500", "3500"};
            int subIndex = random.nextInt(subModels.length);
            String subModel = subModels[subIndex];

            if (model.equalsIgnoreCase("colorado")) {
                return model;
            } else {
                return model + " " + subModel;
            }

        } else if (brand.equalsIgnoreCase("dodge")) {

            final String[] models = {"Ram"};
            Random random = new Random();
            int index = random.nextInt(models.length);
            String model = models[index];

            final String[] subModels = {"1500", "2500", "3500"};
            int subIndex = random.nextInt(subModels.length);
            String subModel = subModels[subIndex];

            return model + " " + subModel;

        } else if (brand.equalsIgnoreCase("honda")) {

            final String[] models = {"Ridgeline"};
            Random random = new Random();
            int index = random.nextInt(models.length);
            String model = models[index];

            return model;

        } else if (brand.equalsIgnoreCase("gmc")) {

            final String[] models = {"Sierra", "Canyon"};
            Random random = new Random();
            int index = random.nextInt(models.length);
            String model = models[index];

            final String[] subModels = {"HD", "1500", "AT4"};
            int subIndex = random.nextInt(subModels.length);
            String subModel = subModels[subIndex];

            if (model.equalsIgnoreCase("canyon")) {
                final String[] canyonSubModels = {"", "Denali"};
                int canyonSubIndex = random.nextInt(canyonSubModels.length);
                String canyonSubModel = canyonSubModels[canyonSubIndex];

                return model + " " + canyonSubModel;

            } else if (subModel.equalsIgnoreCase("hd") || subModel.equalsIgnoreCase("1500")) {
                final String[] sierraSubModels = {"", "Denali"};
                int sierraSubIndex = random.nextInt(sierraSubModels.length);
                String sierraSubModel = sierraSubModels[sierraSubIndex];

                return model + " " + sierraSubModel;
            }
            return model;

        } else if (brand.equalsIgnoreCase("nissan")) {
            final String[] models = {"Frontier", "Titan"};
            Random random = new Random();
            int index = random.nextInt(models.length);
            String model = models[index];

            if (model.equalsIgnoreCase("titan")) {
                final String[] subModels = {"", "XD"};
                int subIndex = random.nextInt(subModels.length);
                String subModel = subModels[subIndex];

                return model + " " + subModel;
            }
            return model;
        }
        return "Unknown";
    }
}
