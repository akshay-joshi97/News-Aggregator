const interests = [
  "technology", "top", "sports", "business", "science",
  "entertainment", "health", "world", "politics", "crime",
  "environment", "tourism", "education"
];

const container = document.getElementById("interestsContainer");
const submitBtn = document.getElementById("submitBtn");

// Get userId from URL
const params = new URLSearchParams(window.location.search);
const userId = params.get("userId");

// Create interest tiles
interests.forEach(interest => {
  const tile = document.createElement("div");
  tile.className = "interest-tile";
  tile.textContent = interest;
  tile.dataset.value = interest.toLowerCase();

  tile.addEventListener("click", () => {
    tile.classList.toggle("selected");
  });

  container.appendChild(tile);
});

// Handle submit
submitBtn.addEventListener("click", () => {
  const selected = Array.from(document.querySelectorAll(".interest-tile.selected"))
    .map(tile => tile.dataset.value);

  if (selected.length === 0) {
    alert("Please select at least one interest.");
    return;
  }

  const data = {
    userId: userId,
    interests: selected
  };
console.log('data>>>', data);
  fetch("http://localhost:4567/updateinterests", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  })
    .then(res => res.json())
    .then(response => {
      if (response.success) {
        alert("Interests saved successfully!");
        window.location.href = `feed.html?userId=${data.userId}`;
      } else {
        alert("Error: " + response.message);
      }
    })
    .catch(error => {
      alert("Failed to send request.");
      console.error(error);
    });
});
