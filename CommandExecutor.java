public class CommandExecutor {
    char[][] fieldmap;
    char[][] tracker;
    int commandCount=0;
    int fuelcost=0;
    int paintPenalty=0;
    int protectedTreePenalty=0;
    int row=-1;
    int column=-1;
    int dir =1;
    int maxRow = 0;
    int maxColumn=0;
    int protectedTreeCount=0;
    StringBuffer commands=new StringBuffer();
    /* dir :
     *   0 = north facing
     *   1 = east facing
     *   2 = west facing
     *   3 = south facing
     *   */
    public CommandExecutor(char[][] fieldmap)
    {
        this.fieldmap=fieldmap;
        this.tracker=new char[fieldmap.length][fieldmap[0].length];
        maxRow=fieldmap.length;
        maxColumn=fieldmap[0].length;
        countProtectedTree();
    }

    private void countProtectedTree() {
        for (int i=0;i<fieldmap.length;i++)
        {
            for(int j=0;j<fieldmap[0].length;j++)
            {
                if(fieldmap[i][j]=='T')
                {protectedTreeCount++;}
            }
        }
    }

    public void executeCommand(String input)
    {
        if(input.equals("l"))
        {
            commandCount++;
            commands.append("turn left, ");
            switch(dir)
            {
                case 0: //north to west
                    dir=2;
                    break;
                case 1: // east to north
                    dir=0;
                    break;
                case 2: // west to south
                    dir=3;
                    break;
                case 3:  //south to east
                    dir=1;
                    break;
                default:
                    break;
            }
            ConstructionSiteSimulator.startStimulator();
        }
        else if(input.equals("r"))
        {
            commandCount++;
            commands.append("turn right, ");
            switch(dir) {
                case 0: //north to east
                    dir = 1; break;
                case 1: //east to south
                    dir = 3; break;
                case 2: //west to north
                    dir = 0; break;
                case 3: // south to west;
                    dir = 2; break;
                default:
                    break;
            }
            ConstructionSiteSimulator.startStimulator();
        }
        else if(input.startsWith("a"))
        {   commandCount++;
            try {
                int steps = Integer.parseInt(input.substring(input.indexOf(" ") + 1));
                commands.append("advance " + steps + ", ");
                navigate(steps);
            }
            catch (NumberFormatException exception)
            {
                System.out.println("Please enter valid command");
                ConstructionSiteSimulator.startStimulator();
            }

        }
        else if(input.equals("q"))
            {
                commands.append("quit");
                calculateTotal();
            }

        else{
            System.out.println("Please enter valid command");
            ConstructionSiteSimulator.startStimulator();
        }
    }
    private int countRemainingBlocks(){
        int result=0;
        for (int i=0;i<tracker.length;i++)
        {
            for(int j=0;j<tracker[0].length;j++)
            {
                if(tracker[i][j]==1)
                {result++;
                }
            }
        }
        return maxRow*maxColumn-result-protectedTreeCount;
    }

    /**
     * Description  :
     * Pasrams :
     * Return :
     */
    private void calculateTotal() {
        System.out.println("The simulation has ended at your request. These are the commands you issued:");
       System.out.println(commands.toString());
       int remainingBlockCount=countRemainingBlocks();
        System.out.println("The costs for this land clearing operation were:");
        System.out.println("Items                                       Quantity        Cost");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("communication overhead                        "+commandCount+"            "+commandCount );
        System.out.println("fuel usage                                    "+fuelcost+"            "+fuelcost);
        System.out.println("uncleared squares                             "+remainingBlockCount+"         "+(remainingBlockCount*3));
        System.out.println("destruction of protected tree                 "+protectedTreePenalty+"            "+(protectedTreePenalty*10));
        System.out.println("paint damage to bulldozer                     "+paintPenalty+"            "+(paintPenalty*2));
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Total                                                   "+(commandCount+fuelcost+(protectedTreePenalty*10)+(paintPenalty*2)+(remainingBlockCount*3)));
        System.out.println("Thank you for using the Aconex site clearing simulator.");
        System.exit(0);
    }

    private void navigate(int steps) {
        switch(dir) {
            case 0: // moving in north
            {
                if(column==-1)
                    column=0;
                if (row-steps>=0) {
                    while (steps > 0) {
                        row--;
                        if (row >= 0) {
                            if (fieldmap[row][column] == 'o')
                                fuelcost = fuelcost + 1;
                            else if (fieldmap[row][column] == 'r') {
                                fuelcost = fuelcost + 2;
                                fieldmap[row][column] = 'o';
                            } else if (fieldmap[row][column] == 't') {
                                fuelcost = fuelcost + 2;
                                fieldmap[row][column] = 'o';
                                if (steps != 1)
                                    paintPenalty++;
                            } else if (fieldmap[row][column] == 'T') {
                                protectedTreePenalty++;
                                executeCommand("q");
                                break;
                            }

                        } else {
                            executeCommand("q");
                            break;
                        }
                        tracker[row][column] = 1;
                        steps--;
                    }
                }
                else {
                    executeCommand("q");
                    break;
                }
                break;
            }
            case 1: // moving in east
            {
                if(row==-1)
                    row=0;
                if(column+steps<maxColumn) {
                    while (steps > 0) {
                        column++;
                        if (column < maxColumn) {
                            if (fieldmap[row][column] == 'o')
                                fuelcost = fuelcost + 1;
                            else if (fieldmap[row][column] == 'r') {
                                fuelcost = fuelcost + 2;
                                fieldmap[row][column] = 'o';
                            } else if (fieldmap[row][column] == 't') {
                                fuelcost = fuelcost + 2;
                                fieldmap[row][column] = 'o';
                                if (steps != 1)
                                    paintPenalty++;
                            } else if (fieldmap[row][column] == 'T') {
                                protectedTreePenalty++;
                                executeCommand("q");
                                break;
                            }

                        } else {
                            executeCommand("q");
                            break;
                        }
                        tracker[row][column] = 1;
                        steps--;
                    }
                }
                else {
                    executeCommand("q");
                    break;
                }
                break;
            }
            case 2: // moving in west
            {
                if(row==-1)
                    row=0;
                if(column-steps>=0) {
                    while (steps > 0) {
                        column--;
                        if (column >= 0) {
                            if (fieldmap[row][column] == 'o')
                                fuelcost = fuelcost + 1;
                            else if (fieldmap[row][column] == 'r') {
                                fuelcost = fuelcost + 2;
                                fieldmap[row][column] = 'o';
                            } else if (fieldmap[row][column] == 't') {
                                fuelcost = fuelcost + 2;
                                fieldmap[row][column] = 'o';
                                if (steps != 1)
                                    paintPenalty++;
                            } else if (fieldmap[row][column] == 'T') {
                                protectedTreePenalty++;
                                executeCommand("q");
                                break;
                            }

                        } else {
                            executeCommand("q");
                            break;
                        }
                        tracker[row][column] = 1;
                        steps--;
                    }
                }
                else {
                    executeCommand("q");
                    break;
                }
                break;
            }
            case 3: // moving in south
            {
                if(column==-1)
                    column=0;
                if(row+steps<maxRow) {
                    while (steps > 0) {
                        row++;
                        if (row < maxRow) {
                            if (fieldmap[row][column] == 'o')
                                fuelcost = fuelcost + 1;
                            else if (fieldmap[row][column] == 'r') {
                                fuelcost = fuelcost + 2;
                                fieldmap[row][column] = 'o';
                            } else if (fieldmap[row][column] == 't') {
                                fuelcost = fuelcost + 2;
                                fieldmap[row][column] = 'o';
                                if (steps != 1) {
                                    paintPenalty++;
                                }
                            } else if (fieldmap[row][column] == 'T') {
                                protectedTreePenalty++;
                                executeCommand("q");
                                break;
                            }

                        } else {
                            executeCommand("q");
                            break;
                        }
                        tracker[row][column] = 1;
                        steps--;
                    }
                }
                else
                    {
                        executeCommand("q");
                        break;
                    }

                break;
            }
        }
        ConstructionSiteSimulator.startStimulator();
    }
}
