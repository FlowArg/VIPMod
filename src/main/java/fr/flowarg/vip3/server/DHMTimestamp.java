package fr.flowarg.vip3.server;

public record DHMTimestamp(int day, int hour, int minute)
{
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        final var dhmTimestamp = (DHMTimestamp)o;

        if (this.day != dhmTimestamp.day) return false;
        if (this.hour != dhmTimestamp.hour) return false;
        return this.minute == dhmTimestamp.minute;
    }

    @Override
    public int hashCode()
    {
        var result = this.day;
        result = 31 * result + this.hour;
        result = 31 * result + this.minute;
        return result;
    }
}
