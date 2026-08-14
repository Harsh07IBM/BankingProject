async function getWeather() {

    const city = document.getElementById("city").value;

    if (!city) {
        alert("Please enter a city");
        return;
    }

    try {

        const response = await fetch(`/weather?city=${city}`);

        const data = await response.json();

        if (data.error) {
            document.getElementById("weather").innerHTML =
                `<p>${data.error}</p>`;
            return;
        }

        document.getElementById("weather").innerHTML = `
            <h2>${data.city}</h2>
            <p>Temperature: ${data.temperature} °C</p>
            <p>Forecast: ${data.forecast}</p>
            <p>Humidity: ${data.humidity}%</p>
        `;

    } catch (error) {

        document.getElementById("weather").innerHTML =
            "<p>Something went wrong.</p>";

    }
}