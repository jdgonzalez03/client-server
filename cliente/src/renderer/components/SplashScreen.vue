<template>
  <div class="splash-container">
    <div class="background-shape"></div>
    <div class="splash-content">
      <div class="logo-container">
        <div class="rocket">🚀</div>
        <div class="splash-logo">
          <span class="logo-text">UniChat</span>
        </div>
      </div>
      <div class="tagline">Conectando conversaciones</div>
      <div class="loading-bar">
        <div class="loading-progress"></div>
      </div>
    </div>
  </div>
</template>
  
<style scoped>
@import '../assets/styles/colors.css';

.splash-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: var(--primary-bg);
  color: var(--text-primary);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  overflow: hidden;
  position: relative;
}

.background-shape {
  position: absolute;
  width: 400px;
  height: 400px;
  background: var(--secondary-bg);
  border-radius: 35% 65% 60% 40% / 45% 45% 55% 55%;
  transform: rotate(-10deg);
  z-index: 1;
  opacity: 0.8;
  box-shadow: 0 0 30px rgba(88, 101, 242, 0.15);
  animation: morphing 15s linear infinite alternate;
}

.splash-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
  position: relative;
  z-index: 2;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.rocket {
  font-size: 3rem;
  animation: rocketShake 2s infinite;
}

.splash-logo {
  font-size: 3.5rem;
  font-weight: bold;
  color: var(--accent);
  animation: fadeIn 1.2s ease-in-out;
  position: relative;
}

.logo-text {
  background: linear-gradient(90deg, var(--accent) 0%, #7984ff 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 0 15px rgba(88, 101, 242, 0.3);
}

.tagline {
  font-size: 1.2rem;
  color: var(--text-secondary);
  letter-spacing: 0.1rem;
  animation: slideUp 1s ease-out 0.3s both;
  opacity: 0;
}

.loading-bar {
  width: 200px;
  height: 4px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 2px;
  overflow: hidden;
  margin-top: 1rem;
}

.loading-progress {
  height: 100%;
  width: 30%;
  background-color: var(--accent);
  border-radius: 2px;
  animation: loading 2s ease-in-out infinite;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes rocketShake {
  0%, 100% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(-5deg);
  }
  75% {
    transform: rotate(5deg);
  }
}

@keyframes loading {
  0% {
    width: 0%;
    transform: translateX(-100%);
  }
  50% {
    width: 30%;
  }
  100% {
    width: 0%;
    transform: translateX(500%);
  }
}

@keyframes morphing {
  0% {
    border-radius: 35% 65% 60% 40% / 45% 45% 55% 55%;
  }
  25% {
    border-radius: 40% 60% 65% 35% / 50% 50% 50% 50%;
  }
  50% {
    border-radius: 45% 55% 50% 50% / 55% 45% 55% 45%;
  }
  75% {
    border-radius: 50% 50% 40% 60% / 40% 60% 40% 60%;
  }
  100% {
    border-radius: 35% 65% 60% 40% / 45% 45% 55% 55%;
  }
}

@media (prefers-reduced-motion) {
  .rocket, .splash-logo, .tagline, .loading-progress, .background-shape {
    animation: none;
  }
  
  .background-shape {
    border-radius: 40% 60% 60% 40% / 50% 45% 55% 50%;
  }
}
</style>